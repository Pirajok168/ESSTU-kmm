package ru.esstu.student.profile.student.porfolio.data.api

import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.data.model.AttestationResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse

class PortfolioApiImpl(
    private val authorizedApi: AuthorizedApi,
    private val fileSystem: FileSystem
): PortfolioApi {

    override suspend fun getFilesPortfolioByType(
        type: PortfolioTypeResponse
    ): Response<List<PortfolioFileRequestResponse>> =
        authorizedApi.get(path = "/lk/api/v1/student/portfolio/getElements?type=${type}")


    override suspend fun saveFilePortfolio(
        type: PortfolioFileRequestResponse,
        files: List<CachedFile>,
    ): Response<PortfolioFileRequestResponse>  {
        return authorizedApi.post("lk/api/v1/student/portfolio/createElement"){
            setBody(MultiPartFormDataContent(
                formData {
                    append("element", value = Json{}.encodeToJsonElement(type).toString() , headers = Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                    })

                    files.forEach {
                        val array = fileSystem.read(it.sourceFile.toPath()){
                            readByteArray()
                        }
                        append("files", array, Headers.build {
                            append(HttpHeaders.ContentType, it.type)
                            append(HttpHeaders.ContentDisposition, "filename=${it.name}.${it.ext}")
                        })
                    }
                }
            ))
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getAttestationMarks(): Response<List<AttestationResponse>>  =
        authorizedApi.get(path = "/lk/api/v1/student/portfolio/getAttestation")

}
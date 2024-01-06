package ru.esstu.student.profile.student.porfolio.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.data.model.AttestationResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse

class PortfolioApiImpl(
    private val portalApi: HttpClient,
    private val fileSystem: FileSystem,
): PortfolioApi {

    override suspend fun getFilesPortfolioByType(
        type: PortfolioTypeResponse,
        authToken: String
    ): List<PortfolioFileRequestResponse> {

        val response = portalApi.get {
            url {
                path("/lk/api/v1/student/portfolio/getElements")
                encodedParameters.append("type", "$type")
                bearerAuth(authToken)
            }
        }

        return response.body()
    }

    override suspend fun saveFilePortfolio(
        type: PortfolioFileRequestResponse,
        files: List<CachedFile>,
        authToken: String,
    ): PortfolioFileRequestResponse {
        val request = portalApi.post {
            headers {
                append("Authorization", "Bearer $authToken")
            }

            url {
                path("lk/api/v1/student/portfolio/createElement")
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

        return request.body()
    }

    override suspend fun getAttestationMarks(authToken: String): List<AttestationResponse> {
        val response = portalApi.get {
            url {
                path("/lk/api/v1/student/portfolio/getAttestation")
                bearerAuth(authToken)
            }
        }

        return response.body()
    }
}
package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api

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
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentThemeResponse

class NewAppealApiImpl(
    private val authorizedApi: AuthorizedApi,
    private val fileSystem: FileSystem,
): NewAppealApi {
    override suspend fun getDepartments(): Response<List<DepartmentResponse>> {
        return authorizedApi.get("lk/api/v1/appeal/getAppealsDepartment")
    }

    override suspend fun getDepartmentsThemes(
        code: String
    ): Response<List<DepartmentThemeResponse>> {
        return authorizedApi.get("lk/api/v1/appeal/getAppealsByDepartment?departmentCode=$code")
    }

    override suspend fun createAppealChat(
        body: NewAppealRequestBody
    ): Response<DataResponse> {
        return authorizedApi.post("lk/api/v2/messenger/createChat", body = body){
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun createAppealChatWithAttachments(
        files: List<CachedFile>,
        body: NewAppealRequestBody
    ): Response<DataResponse> {
        return authorizedApi.post("lk/api/v2/messenger/createChatMedia", body = body){
            setBody(
                MultiPartFormDataContent(
                    formData{
                        append("requestCreateChat", value = Json{}.encodeToJsonElement(body).toString() , headers = Headers.build {
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
                )
            )
            contentType(ContentType.Application.Json)
        }
    }
}
package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentThemeResponse

class NewAppealApiImpl(
    private val portalApi: HttpClient,
    private val fileSystem: FileSystem,
): NewAppealApi {
    override suspend fun getDepartments(authToken: String): List<DepartmentResponse> {
       val response = portalApi.get {
           url {
               path("lk/api/v1/appeal/getAppealsDepartment")
               bearerAuth(authToken)
           }
       }

        return response.body()
    }

    override suspend fun getDepartmentsThemes(
        authToken: String,
        code: String
    ): List<DepartmentThemeResponse> {
        val response = portalApi.get {
            url {
                path("lk/api/v1/appeal/getAppealsByDepartment")
                bearerAuth(authToken)
                encodedParameters.append("departmentCode", code)
            }
        }

        return response.body()
    }

    override suspend fun createAppealChat(
        authToken: String,
        body: NewAppealRequestBody
    ): DataResponse {
        val request = portalApi.post {
            url{
                path("mlk/api/v2/messenger/createChat")
                bearerAuth(authToken)
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
        return request.body()
    }

    override suspend fun createAppealChatWithAttachments(
        authToken: String,
        files: List<CachedFile>,
        body: NewAppealRequestBody
    ): DataResponse {
        val request = portalApi.post {
            headers {
                append("Authorization", "Bearer $authToken")
            }

            url{
                path("mlk/api/v2/messenger/createChatMedia")
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
        return request.body()
    }
}
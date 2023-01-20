package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api

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
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.response.SupportGroup

class NewSupportApiImpl(
    private val portalApi: HttpClient,
    private val fileSystem: FileSystem,
): NewSupportApi {
    override suspend fun getSupport(authToken: String): List<SupportGroup> {
        val response =  portalApi.get {
            url {
                path("lk/api/v1/users/getSupport")
                bearerAuth(authToken)
            }
        }


        return response.body()
    }

    override suspend fun createSupportChat(
        authToken: String,
        body: NewSupportRequestBody
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

    override suspend fun createSupportChatWithAttachments(
        authToken: String,
        files: List<CachedFile>,
        body: NewSupportRequestBody
    ): DataResponse {
        val request = portalApi.post {
            headers {
                append("Authorization", "Bearer $authToken")
            }

            url{
                path("lk/api/v2/messenger/createChatMedia")
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
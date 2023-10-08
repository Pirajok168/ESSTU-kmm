package ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.student.messaging.entities.CachedFile
import kotlin.collections.get

class NewDialogApiImpl(
    private val portalApi: HttpClient,
    private val fileSystem: FileSystem,
): NewDialogApi {
    override suspend fun findUsers(
        authToken: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<UserPreview> {
        val response = portalApi.get("lk/api/v1/users/findUsers"){
            url{
                bearerAuth(authToken)
                encodedParameters.append("query", query)
                encodedParameters.append("limit", limit.toString())
                encodedParameters.append("offset", offset.toString())
            }
        }
        return response.body()
    }

    override suspend fun pickMessages(authToken: String, messageIds: String): List<MessagePreview> {
        val request = portalApi.get {
            url {
                path("lk/api/v2/messenger/getMessages")
                bearerAuth(authToken)
                encodedParameters.append("id", messageIds)
            }
        }
        return request.body()
    }

    override suspend fun pickUsers(authToken: String, usersIds: String): List<UserPreview> {
        val request = portalApi.get {
            url {
                path("lk/api/v1/users/getUsers")
                bearerAuth(authToken)
                encodedParameters.append("ids", usersIds)
            }
        }
        return request.body()
    }

    override suspend fun sendMessage(
        authToken: String,
        body: ChatMessageRequestBody
    ): ChatMessageResponse {
        val request = portalApi.post {
            url {
                path("/lk/api/v2/messenger/sendMessage")
                bearerAuth(authToken)
                contentType(ContentType.Application.Json)
                setBody(body)

            }

        }
        return request.body()
    }

    override suspend fun sendMessageWithAttachments(
        authToken: String,
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): ChatMessageResponse {
        val request = portalApi.post {
            headers {
                append("Authorization", "Bearer $authToken")
            }

            url {
                path("lk/api/v2/messenger/sendMessageMedia")
                setBody(MultiPartFormDataContent(
                    formData {
                        append("requestSendMessage", value = Json{}.encodeToJsonElement(requestSendMessage).toString() , headers = Headers.build {
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

    override suspend fun sendAttachments(
        authToken: String,
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): ChatMessageResponse {
        val request = portalApi.post {
            headers {
                append("Authorization", "Bearer $authToken")
            }

            url {
                path("lk/api/v2/messenger/sendMessageMedia")
                setBody(MultiPartFormDataContent(
                    formData {
                        append("requestSendMessage", value = Json{}.encodeToJsonElement(requestSendMessage).toString() , headers = Headers.build {
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

}
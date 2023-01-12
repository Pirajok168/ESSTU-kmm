package ru.esstu.student.messaging.group_chat.datasources.api

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
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatReadRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.modules.account.datasources.api.response.UserResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.group_chat.datasources.api.response.ConversationResponse
import kotlin.collections.get

class GroupChatApiImpl(
    private val portalApi: HttpClient,
    private val fileSystem: FileSystem,
): GroupChatApi {
    override suspend fun getOpponent(authToken: String, userId: String): UserResponse {
        val response = portalApi.get{
            url{
                path("mlk/api/v2/messenger/getUserFull")
                bearerAuth(authToken)
                encodedParameters.append("id", userId)
            }
        }
        return response.body()
    }

    override suspend fun getConversation(authToken: String, id: String): ConversationResponse {
        val response = portalApi.get{
            url{
                path("mlk/api/v2/messenger/getChatFull")
                bearerAuth(authToken)
                encodedParameters.append("id", id)
            }
        }
        val a = response.bodyAsText()
        a
        return response.body()
    }

    override suspend fun readMessages(authToken: String, body: ChatReadRequestBody): Boolean {
        val request = portalApi.post {
            url {
                path("mlk/api/v2/messenger/readHistory")
                bearerAuth(authToken)
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
        return request.body()
    }

    override suspend fun getHistory(
        authToken: String,
        peerId: String,
        offset: Int,
        limit: Int
    ): MessageResponse {
        val request = portalApi.get {
            url {
                path("mlk/api/v2/messenger/getHistory")
                bearerAuth(authToken)
                encodedParameters.append("peerId", peerId)
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())

            }
        }
        request
        return request.body()
    }

    override suspend fun pickMessages(authToken: String, messageIds: String): List<MessagePreview> {
        val request = portalApi.get {
            url {
                path("mlk/api/v2/messenger/getMessages")
                bearerAuth(authToken)
                encodedParameters.append("id", messageIds)
            }
        }
        return request.body()
    }

    override suspend fun pickUsers(authToken: String, usersIds: String): List<UserPreview> {
        val request = portalApi.get {
            url {
                path("mlk/api/v1/users/getUsers")
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
                path("mlk/api/v2/messenger/sendMessage")
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
                path("mlk/api/v2/messenger/sendMessageMedia")
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
                path("mlk/api/v2/messenger/sendMessageMedia")
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
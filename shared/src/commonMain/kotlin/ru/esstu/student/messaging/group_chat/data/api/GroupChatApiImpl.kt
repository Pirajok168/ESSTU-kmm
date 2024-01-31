package ru.esstu.student.messaging.group_chat.data.api

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
import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatReadRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.features.account.data.model.UserResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.group_chat.data.api.response.ConversationResponse

class GroupChatApiImpl(
    private val authorizedApi: AuthorizedApi,
    private val fileSystem: FileSystem,
) : GroupChatApi {
    override suspend fun getOpponent(userId: String): Response<UserResponse> {
        return authorizedApi.get("lk/api/v2/messenger/getUserFull?id=$userId")
    }

    override suspend fun getConversation(id: String): Response<ConversationResponse> {
        return authorizedApi.get("lk/api/v2/messenger/getChatFull?id=$id")
    }

    override suspend fun readMessages(body: ChatReadRequestBody): Response<Boolean> {
        return authorizedApi.post("lk/api/v2/messenger/readHistory", body = body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun getHistory(
        peerId: String,
        offset: Int,
        limit: Int
    ): Response<MessageResponse> {
        return authorizedApi.get("lk/api/v2/messenger/getHistory?peerId=$peerId&offset=${offset}&limit=${limit}")
    }

    override suspend fun pickMessages(messageIds: String): Response<List<MessagePreview>> {
        return authorizedApi.get("lk/api/v2/messenger/getMessages?id=$messageIds")
    }

    override suspend fun pickUsers(usersIds: String): Response<List<UserPreview>> {
        return authorizedApi.get("lk/api/v1/users/getUsers?ids=$usersIds")
    }

    override suspend fun sendMessage(
        body: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {
        return authorizedApi.post("lk/api/v2/messenger/sendMessage", body = body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun sendMessageWithAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {
        return authorizedApi.post("lk/api/v2/messenger/sendMessageMedia") {
            setBody(MultiPartFormDataContent(
                formData {
                    append(
                        "requestSendMessage",
                        value = Json {}.encodeToJsonElement(requestSendMessage).toString(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        })

                    files.forEach {
                        val array = fileSystem.read(it.sourceFile.toPath()) {
                            readByteArray()
                        }
                        append("files", array, Headers.build {
                            append(HttpHeaders.ContentType, it.type)
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=${it.name}.${it.ext}"
                            )
                        })
                    }
                }
            ))
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun sendAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {

        return authorizedApi.post("lk/api/v2/messenger/sendMessageMedia") {
            setBody(MultiPartFormDataContent(
                formData {
                    append(
                        "requestSendMessage",
                        value = Json {}.encodeToJsonElement(requestSendMessage).toString(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                        })

                    files.forEach {

                        val array = fileSystem.read(it.sourceFile.toPath()) {
                            readByteArray()
                        }
                        append("files", array, Headers.build {
                            append(HttpHeaders.ContentType, it.type)
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=${it.name}.${it.ext}"
                            )
                        })
                    }
                }
            ))
            contentType(ContentType.Application.Json)
        }
    }
}
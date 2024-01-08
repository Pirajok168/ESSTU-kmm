package ru.esstu.student.messaging.dialog_chat.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

import io.ktor.util.*
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
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.modules.account.datasources.api.response.UserResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile


class DialogChatApiImpl(
    private val authorizedApi: AuthorizedApi,
    private val fileSystem: FileSystem,
): DialogChatApi {
    override suspend fun getOpponent(userId: String): Response<UserResponse> {
        return authorizedApi.get("lk/api/v2/messenger/getUserFull?id=${userId}")
    }

    override suspend fun readMessages(body: ChatReadRequestBody): Response<Boolean>  {
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
        return authorizedApi.post("lk/api/v2/messenger/sendMessage", body = body){
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun sendMessageWithAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {


        return authorizedApi.post("lk/api/v2/messenger/sendMessageMedia"){
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

    @OptIn(InternalAPI::class)
    override suspend fun sendAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {

        return authorizedApi.post("lk/api/v2/messenger/sendMessageMedia"){
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
}
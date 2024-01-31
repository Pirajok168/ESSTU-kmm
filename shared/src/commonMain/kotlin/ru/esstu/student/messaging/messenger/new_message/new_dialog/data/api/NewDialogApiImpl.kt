package ru.esstu.student.messaging.messenger.new_message.new_dialog.data.api

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
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.student.messaging.entities.CachedFile

class NewDialogApiImpl(
    private val authorizedApi: AuthorizedApi,
    private val fileSystem: FileSystem,
) : NewDialogApi {
    override suspend fun findUsers(
        query: String,
        limit: Int,
        offset: Int
    ): Response<List<UserPreview>> {
        return authorizedApi.get("lk/api/v1/users/findUsers?query=$query&limit=$limit&offset=$offset")
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
        return authorizedApi.post("/lk/api/v2/messenger/sendMessage", body = body) {
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun sendMessageWithAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse> {
        return authorizedApi.post("/lk/api/v2/messenger/sendMessage") {
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
package ru.esstu.student.messaging.messenger.new_message.new_dialog.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.student.messaging.entities.CachedFile

interface NewDialogApi {

    suspend fun findUsers(
        query: String,
        limit: Int,
        offset: Int
    ): Response<List<UserPreview>>


    suspend fun pickMessages(
        messageIds: String,
    ): Response<List<MessagePreview>>


    suspend fun pickUsers(
        usersIds: String,
    ): Response<List<UserPreview>>

    //region отправка сообщений

    suspend fun sendMessage(
        body: ChatMessageRequestBody
    ): Response<ChatMessageResponse>

    suspend fun sendMessageWithAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse>


    suspend fun sendAttachments(
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): Response<ChatMessageResponse>
    //endregion
}
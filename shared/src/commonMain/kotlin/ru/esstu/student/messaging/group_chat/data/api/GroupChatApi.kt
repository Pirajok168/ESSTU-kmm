package ru.esstu.student.messaging.group_chat.data.api


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

interface GroupChatApi {


    suspend fun getConversation(
        id: String,
    ): Response<ConversationResponse>


    suspend fun getOpponent(
        userId: String,
    ): Response<UserResponse>


    suspend fun readMessages(
        body: ChatReadRequestBody
    ): Response<Boolean>


    suspend fun getHistory(
        peerId: String,
        offset: Int,
        limit: Int
    ): Response<MessageResponse>


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
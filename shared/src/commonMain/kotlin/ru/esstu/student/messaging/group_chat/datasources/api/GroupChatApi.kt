package ru.esstu.student.messaging.group_chat.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatReadRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.modules.account.datasources.api.response.UserResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.group_chat.datasources.api.response.ConversationResponse

interface GroupChatApi {



    suspend fun getConversation(
         authToken: String,
        id: String,
    ): ConversationResponse


    suspend fun getOpponent(
        authToken: String,
        userId: String,
    ): UserResponse


    suspend fun readMessages(
        authToken: String,
        body: ChatReadRequestBody
    ): Boolean


    suspend fun getHistory(
         authToken: String,
         peerId: String,
        offset: Int,
         limit: Int
    ): MessageResponse


    suspend fun pickMessages(
         authToken: String,
         messageIds: String,
    ): List<MessagePreview>


    suspend fun pickUsers(
         authToken: String,
         usersIds: String,
    ): List<UserPreview>


    //region отправка сообщений

    suspend fun sendMessage(
        authToken: String,
        body: ChatMessageRequestBody
    ): ChatMessageResponse


    suspend fun sendMessageWithAttachments(
        authToken: String,
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): ChatMessageResponse


    suspend fun sendAttachments(
        authToken: String,
        files: List<CachedFile>,
        requestSendMessage: ChatMessageRequestBody
    ): ChatMessageResponse
    //endregion
}
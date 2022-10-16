package ru.esstu.student.messaging.dialog_chat.datasources.api


import io.ktor.client.request.forms.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response.ChatMessageResponse
import ru.esstu.student.messaging.dialog_chat.datasources.api.request.ReadRequest
import ru.esstu.student.messaging.dialog_chat.datasources.api.response.MessageResponse
import ru.esstu.student.messaging.dialog_chat.datasources.api.response.UserResponse
import ru.esstu.student.messaging.messenger.datasources.api.response.Message
import ru.esstu.student.messaging.messenger.datasources.api.response.User

interface DialogChatApi {



    suspend fun getOpponent(
        authToken: String,
         userId: String,
    ): UserResponse


    suspend fun readMessages(
         authToken: String,
       body: ReadRequest
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
    ):List<Message>


    suspend fun pickUsers(
         authToken: String,
        usersIds: String,
    ):List<User>


    //region отправка сообщений

    suspend fun sendMessage(
        authToken: String,
        body: ChatMessageRequestBody
    ): ChatMessageResponse


    suspend fun sendMessageWithAttachments(
        authToken: String,
        files: List<MultiPartFormDataContent>,
        requestSendMessage: ChatMessageRequestBody
    ): ChatMessageResponse


    suspend fun sendAttachments(
        authToken: String,
        files: List<MultiPartFormDataContent>,
        requestSendMessage: ChatRequestBody
    ): ChatMessageResponse
    //endregion
}
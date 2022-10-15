package ru.esstu.student.messaging.dialog_chat.datasources.api

import ru.esstu.student.messaging.dialog_chat.datasources.api.response.MessageResponse

interface DialogChatUpdateApi {
    suspend fun getUpdates(
        authToken: String,
        peerId: String,
         lastMessageId: Long
    ): MessageResponse
}
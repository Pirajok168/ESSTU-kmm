package ru.esstu.student.messaging.dialog_chat.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.utill.wrappers.Response


interface DialogChatUpdateApi {
    suspend fun getUpdates(
        peerId: String,
         lastMessageId: Long
    ): Response<MessageResponse>
}
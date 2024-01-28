package ru.esstu.student.messaging.group_chat.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.utill.wrappers.Response

interface GroupChatUpdateApi {
    suspend fun getUpdates(
        peerId: String,
        lastMessageId: Long
    ): Response<MessageResponse>
}
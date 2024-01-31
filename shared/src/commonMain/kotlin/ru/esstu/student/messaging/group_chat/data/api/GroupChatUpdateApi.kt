package ru.esstu.student.messaging.group_chat.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse

interface GroupChatUpdateApi {
    suspend fun getUpdates(
        peerId: String,
        lastMessageId: Long
    ): Response<MessageResponse>
}
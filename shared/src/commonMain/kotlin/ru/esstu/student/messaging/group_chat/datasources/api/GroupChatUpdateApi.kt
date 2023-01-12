package ru.esstu.student.messaging.group_chat.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse

interface GroupChatUpdateApi {
    suspend fun getUpdates(
        authToken: String,
        peerId: String,
        lastMessageId: Long
    ): MessageResponse
}
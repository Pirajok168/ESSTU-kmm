package ru.esstu.student.messaging.group_chat.datasources.api

import ru.esstu.student.messaging.group_chat.datasources.api.response.MessageResponse

interface GroupChatUpdateApi {
    companion object {
        const val BASE_URL = "https://esstu.ru"
    }


    suspend fun getUpdates(
         authToken: String,
        peerId: String,
         lastMessageId: Long
    ): MessageResponse
}
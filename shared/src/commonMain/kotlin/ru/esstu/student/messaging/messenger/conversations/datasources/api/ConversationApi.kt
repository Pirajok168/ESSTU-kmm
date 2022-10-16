package ru.esstu.student.messaging.messenger.conversations.datasources.api


import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

interface ConversationApi {



    suspend fun getConversations(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse
}
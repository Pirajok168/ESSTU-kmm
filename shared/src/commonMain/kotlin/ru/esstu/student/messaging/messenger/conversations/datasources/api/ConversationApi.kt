package ru.esstu.student.messaging.messenger.conversations.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface ConversationApi {
    suspend fun getConversations(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse
}
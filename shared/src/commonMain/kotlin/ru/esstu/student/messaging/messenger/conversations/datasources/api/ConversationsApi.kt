package ru.esstu.student.messaging.messenger.conversations.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response

interface ConversationsApi {
    suspend fun getConversations(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
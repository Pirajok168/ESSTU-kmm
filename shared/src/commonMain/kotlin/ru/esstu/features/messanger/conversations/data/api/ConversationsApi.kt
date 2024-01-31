package ru.esstu.features.messanger.conversations.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface ConversationsApi {
    suspend fun getConversations(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
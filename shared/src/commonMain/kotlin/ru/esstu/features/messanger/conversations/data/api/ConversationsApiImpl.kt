package ru.esstu.features.messanger.conversations.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class ConversationsApiImpl(
    private val authorizedApi: AuthorizedApi,
) : ConversationsApi {
    override suspend fun getConversations(
        offset: Int,
        limit: Int
    ): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=CHAT&offset=$offset&limit=$limit")
    }
}
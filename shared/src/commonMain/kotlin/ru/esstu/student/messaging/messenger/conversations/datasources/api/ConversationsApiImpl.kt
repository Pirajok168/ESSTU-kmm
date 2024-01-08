package ru.esstu.student.messaging.messenger.conversations.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

class ConversationsApiImpl(
    private val authorizedApi: AuthorizedApi,
): ConversationsApi {
    override suspend fun getConversations(
        offset: Int,
        limit: Int
    ): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=CHAT&offset=$offset&limit=$limit")
    }
}
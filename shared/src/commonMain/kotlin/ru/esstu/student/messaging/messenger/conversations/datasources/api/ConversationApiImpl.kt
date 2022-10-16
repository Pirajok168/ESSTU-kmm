package ru.esstu.student.messaging.messenger.conversations.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApi
import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

class ConversationApiImpl(
    private val portalApi: HttpClient
): ConversationApi {
    override suspend fun getConversations(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/v2/messenger/getDialogs?type=CHAT")
                bearerAuth(authToken)
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
package ru.esstu.student.messaging.messenger.conversations.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import kotlin.collections.get

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
                path("/mlk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "CHAT")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
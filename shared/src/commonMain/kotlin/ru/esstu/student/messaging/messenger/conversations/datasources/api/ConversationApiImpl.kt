package ru.esstu.student.messaging.messenger.conversations.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
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
                path("lk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "CHAT")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }.decodeFromJsonElement(response.body())
    }
}
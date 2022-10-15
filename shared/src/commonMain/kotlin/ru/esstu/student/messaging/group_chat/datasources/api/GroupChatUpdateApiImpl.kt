package ru.esstu.student.messaging.group_chat.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.student.messaging.group_chat.datasources.api.response.MessageResponse

class GroupChatUpdateApiImpl(
    private val portalApi: HttpClient,
) : GroupChatUpdateApi {
    override suspend fun getUpdates(
        authToken: String,
        peerId: String,
        lastMessageId: Long
    ): MessageResponse {
        val response = portalApi.get {
            url {
                path("lk/api/async/messenger/getDialogUpdates")
                bearerAuth(authToken)
                encodedParameters.append("peerId", peerId)
                encodedParameters.append("lastMessageId", lastMessageId.toString())

            }
        }
        return response.body()
    }
}
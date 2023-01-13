package ru.esstu.student.messaging.group_chat.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApi

class GroupChatUpdateApiImpl(
    private val portalApi: HttpClient,
): GroupChatUpdateApi {
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
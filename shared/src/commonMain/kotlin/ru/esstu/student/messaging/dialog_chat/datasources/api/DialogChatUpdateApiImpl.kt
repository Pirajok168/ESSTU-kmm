package ru.esstu.student.messaging.dialog_chat.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response


class DialogChatUpdateApiImpl(
    private val authorizedApi: AuthorizedApi,
) : DialogChatUpdateApi {
    override suspend fun getUpdates(
        peerId: String,
        lastMessageId: Long
    ): Response<MessageResponse> {
        return authorizedApi.get("lk/api/async/messenger/getDialogUpdates?peerId=$peerId&lastMessageId=$lastMessageId")
    }

}
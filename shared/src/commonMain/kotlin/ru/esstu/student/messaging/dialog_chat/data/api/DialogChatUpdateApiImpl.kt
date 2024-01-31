package ru.esstu.student.messaging.dialog_chat.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse


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
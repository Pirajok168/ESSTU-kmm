package ru.esstu.student.messaging.group_chat.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse

class GroupChatUpdateApiImpl(
    private val authorizedApi: AuthorizedApi,
) : GroupChatUpdateApi {
    override suspend fun getUpdates(
        peerId: String,
        lastMessageId: Long
    ): Response<MessageResponse> {
        return authorizedApi.get(path = "lk/api/async/messenger/getDialogUpdates?peerId$peerId&lastMessageId=$lastMessageId")
    }

}
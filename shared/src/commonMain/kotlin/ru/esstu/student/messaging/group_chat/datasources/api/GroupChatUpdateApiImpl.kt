package ru.esstu.student.messaging.group_chat.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

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
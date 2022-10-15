package ru.esstu.student.messaging.group_chat.datasources.repo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApi
import ru.esstu.student.messaging.group_chat.datasources.toMessages


class GroupChatUpdateRepositoryImpl constructor(
    private val auth: IAuthRepository,
    private val updateApi: GroupChatUpdateApi,
    private val chatApi: GroupChatApi
) : IDialogChatUpdateRepository {
    override fun collectUpdates(convId: Int, lastMessageId: Long) = flow {
        var latestMessageId = lastMessageId
        while (true) {

            when (val result = auth.provideToken { type, token ->
                val updates = updateApi.getUpdates("$type $token", lastMessageId = latestMessageId, peerId = convId.toString())

                updates.toMessages(
                    provideUsers = { indices ->
                        chatApi.pickUsers("$type $token", indices.joinToString())
                    },
                    provideReplies = { indices ->
                        chatApi.pickMessages("$type $token", indices.joinToString())
                    }
                )

            }) {
                is Response.Error -> {
                    emit(Response.Error(result.error))
                    delay(500)
                }
                is Response.Success -> {
                    val messages = result.data.map { it.copy(attachments = it.attachments.asReversed()) }.asReversed()
                    latestMessageId =
                        messages.lastOrNull { it.status == DeliveryStatus.DELIVERED }?.id ?: messages.firstOrNull()?.id ?: latestMessageId

                    emit(Response.Success(messages))
                }
            }
        }
    }
}
package ru.esstu.student.messaging.group_chat.datasources.repo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.dialog_chat.datasources.toMessages
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatUpdateApi

class GroupChatUpdateRepositoryImpl(
    private val auth: IAuthRepository,
    private val updateApi: GroupChatUpdateApi,
    private val chatApi: GroupChatApi
): IGroupChatUpdateRepository {
    override fun collectUpdates(convId: Int, lastMessageId: Long): Flow<Response<List<Message>>> = flow{
        var latestMessageId = lastMessageId
        while (true) {

            when (val result = auth.provideToken { type, token ->
                val updates = updateApi.getUpdates("$token", lastMessageId = latestMessageId, peerId = convId.toString())

                updates.toMessages(
                    provideUsers = { indices ->
                        chatApi.pickUsers("$token", indices.joinToString())
                    },
                    provideReplies = { indices ->
                        chatApi.pickMessages("$token", indices.joinToString())
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
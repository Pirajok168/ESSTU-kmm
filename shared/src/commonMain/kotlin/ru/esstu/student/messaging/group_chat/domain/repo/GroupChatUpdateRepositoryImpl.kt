package ru.esstu.student.messaging.group_chat.domain.repo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.dialog_chat.domain.toMessages
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.group_chat.data.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.data.api.GroupChatUpdateApi

class GroupChatUpdateRepositoryImpl(
    private val updateApi: GroupChatUpdateApi,
    private val chatApi: GroupChatApi
) : IGroupChatUpdateRepository {
    override fun collectUpdates(convId: Int, lastMessageId: Long): Flow<Response<List<Message>>> =
        flow {
            var latestMessageId = lastMessageId
            while (true) {

                when (val result = updateApi.getUpdates(
                    peerId = convId.toString(),
                    lastMessageId = latestMessageId
                )
                    .transform {
                        it.toMessages(
                            provideUsers = { indices ->
                                chatApi.pickUsers(indices.joinToString()).data.orEmpty()
                            },
                            provideReplies = { indices ->
                                chatApi.pickMessages(indices.joinToString()).data.orEmpty()
                            }
                        )
                    }) {
                    is Response.Error -> {
                        emit(Response.Error(result.error))
                        delay(500)
                    }

                    is Response.Success -> {
                        val messages =
                            result.data.map { it.copy(attachments = it.attachments.asReversed()) }
                                .asReversed()
                        latestMessageId =
                            messages.lastOrNull { it.status == DeliveryStatus.DELIVERED }?.id
                                ?: messages.firstOrNull()?.id ?: latestMessageId

                        emit(Response.Success(messages))
                    }
                }
            }
        }
}
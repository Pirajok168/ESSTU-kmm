package ru.esstu.student.messaging.dialog_chat.datasources.repo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatUpdateApi
import ru.esstu.student.messaging.dialog_chat.datasources.toMessages
import ru.esstu.student.messaging.entities.DeliveryStatus


class DialogChatUpdateRepositoryImpl constructor(
    private val auth: IAuthRepository,
    private val updateApi: DialogChatUpdateApi,
    private val chatApi: DialogChatApi
) : IDialogChatUpdateRepository {
    override fun collectUpdates(dialogId: String, lastMessageId: Long) = flow {
        var latestMessageId = lastMessageId
        while (true) {

            when (val result = auth.provideToken { type, token ->
                val updates = updateApi.getUpdates(
                    "$token",
                    lastMessageId = latestMessageId,
                    peerId = dialogId
                )

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
                    val messages =
                        result.data.map { it.copy(attachments = emptyList()) }
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
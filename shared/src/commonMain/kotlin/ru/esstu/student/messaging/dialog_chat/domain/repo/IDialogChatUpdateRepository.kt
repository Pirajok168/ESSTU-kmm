package ru.esstu.student.messaging.dialog_chat.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.Message

interface IDialogChatUpdateRepository {
    fun collectUpdates(dialogId: String, lastMessageId: Long = 0): Flow<Response<List<Message>>>
}
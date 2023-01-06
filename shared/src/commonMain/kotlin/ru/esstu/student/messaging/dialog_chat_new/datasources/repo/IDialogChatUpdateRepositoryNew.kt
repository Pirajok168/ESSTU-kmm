package ru.esstu.student.messaging.dialog_chat_new.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.Message

interface IDialogChatUpdateRepositoryNew {
    fun collectUpdates(dialogId: String, lastMessageId: Long = 0): Flow<Response<List<Message>>>
}
package ru.esstu.student.messaging.messenger.dialogs.datasources.db


import kotlinx.coroutines.flow.MutableSharedFlow
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

interface CacheDao {
    val cachedDialogs: MutableSharedFlow<List<Dialog>>
    suspend fun setLastMessage(message: Message)
    suspend fun setDialog(appUserId: String, dialog: ru.esstu.student.messaging.messenger.dialogs.entities.Dialog)
    suspend fun getDialogWithLastMessage(appUserId: String, pageSize: Int, pageOffset: Int): List<DialogWithMessage>

}
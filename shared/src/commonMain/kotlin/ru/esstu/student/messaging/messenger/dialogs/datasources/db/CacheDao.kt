package ru.esstu.student.messaging.messenger.dialogs.datasources.db


import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewLastMessage

interface CacheDao {
    suspend fun setLastMessage(message: PreviewLastMessage)
    suspend fun setDialog(appUserId: String, previewDialog: PreviewDialog)
    suspend fun getDialogWithLastMessage(appUserId: String, pageSize: Int, pageOffset: Int): List<DialogWithMessage>

    suspend fun updateDialogLastMessage(appUserId: String, dialogId: String, lastMessage: PreviewLastMessage)

    suspend fun deleteDialog(idDialog: Long)
}
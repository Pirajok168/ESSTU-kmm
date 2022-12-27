package ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache


import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.AttachmentEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.entities.relations.DialogWithMessage


abstract class CacheDao {


    abstract suspend fun getMessageWithAttachments(id: Long): MessageWithAttachments


    abstract suspend fun getDialogs(appUserId: String, pageSize: Int, pageOffset: Int): List<DialogEntity>


    abstract suspend fun getDialog(appUserId: String, Id: String): DialogEntity?


    open suspend fun getDialogsWithLastMessage(appUserId: String, pageSize: Int, pageOffset: Int): List<DialogWithMessage> {
        val rawDialogs = getDialogs(appUserId, pageSize, pageOffset)
        val dialogs = rawDialogs.map { dialog ->
            DialogWithMessage(
                dialog = dialog,
                lastMessage = dialog.lastMessageId?.let { id -> getMessageWithAttachments(id) }
            )
        }
        return dialogs
    }

    abstract suspend fun setDialogs(dialogs: List<DialogEntity>)


    abstract suspend fun setMessage(message: MessageEntity)


    abstract suspend fun clearMessage(id: Long)

    abstract suspend fun setAttachments(attachments: List<AttachmentEntity>)

    abstract suspend fun clearAttachments(messageId: Long)


    open suspend fun setDialogsWithLastMessage(appUserId: String, dialogs: List<DialogWithMessage>) {

        dialogs.forEach { dialog ->
            val message = dialog.lastMessage?.message
            val attachments = dialog.lastMessage?.attachments.orEmpty()

            val oldDialog = getDialog(dialog.dialog.id, appUserId)

            if (oldDialog?.lastMessageId != null) {
                clearMessage(oldDialog.lastMessageId)
                clearAttachments(oldDialog.lastMessageId)
            }

            if (message != null) {
                setMessage(message)
                setAttachments(attachments)
            }
        }

        setDialogs(dialogs.map { it.dialog })
    }


    abstract suspend fun updateDialog(appUserId: String, dialogId: String, lastMessageId: Long)

    open suspend fun updateDialogLastMessage(appUserId: String, dialogId: String, message: MessageWithAttachments) {
        setAttachments(message.attachments)
        setMessage(message.message)

        updateDialog(dialogId = dialogId, appUserId = appUserId, lastMessageId = message.message.id)
    }

    abstract suspend fun clearDialogs()

    abstract suspend fun clearMessages()

    abstract suspend fun clearAttachments()

    open suspend fun clearAll() {
        clearDialogs()
        clearMessages()
        clearAttachments()
    }
}
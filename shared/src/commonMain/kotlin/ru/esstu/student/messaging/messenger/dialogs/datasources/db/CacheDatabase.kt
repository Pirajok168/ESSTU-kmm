package ru.esstu.student.messaging.messenger.dialogs.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.toReplyMessageEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUserEntity
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

class CacheDatabase(
    database: EsstuDatabase,
): CacheDao {
    private val dbQueries = database.dialogsTableNewQueries






    override suspend fun setLastMessage(message: PreviewLastMessage) {
        message.apply {
            dbQueries.setLastMessage(id, from.toUserEntity(), date,message.message, status.name,replyMessage?.toReplyMessageEntity(), attachments)
        }
    }

    override suspend fun setDialog(appUserId: String, previewDialog: PreviewDialog) {
        previewDialog.apply {
            dbQueries.setDialog(id,appUserId, lastMessage?.id, opponent.toUserEntity(), unreadMessageCount, notifyAboutIt)
        }
    }

    override suspend fun getDialogWithLastMessage(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<DialogWithMessage> {
        fun map(
            idDialog: String,
            appUserId: String,
            lastMessageId: Long?,
            opponent: UserEntity,
            unread: Int,
            notifyAboutIt: Boolean,
            messageId: Long?,
            fromUser: UserEntity?,
            date: Long?,
            message: String?,
            status: String?,
            replyMessage: ReplyMessageEntity?,
            countAttachments: Int?
        ): DialogWithMessage{
            return DialogWithMessage(
                dialog = DialogEntity(
                    appUserId,
                    idDialog,
                    lastMessageId,
                    opponent,
                    unread,
                    notifyAboutIt
                ),
                lastMessage = LastMessageWithCountAttachments(
                    message = MessageEntity(
                        messageId!!,
                        fromUser!!,
                        date!!,
                        message,
                        status!!,
                        replyMessage,
                    ),
                    attachments = countAttachments!!
                )
            )
        }
       return dbQueries.getDialogsWithLastMessage(pageSize.toLong(), pageOffset.toLong(), ::map).executeAsList()
    }

    override suspend fun updateDialogLastMessage(
        appUserId: String,
        dialogId: String,
        lastMessage: PreviewLastMessage
    ) {
        val dialog = dbQueries.getDialog(dialogId, appUserId).executeAsOneOrNull()
        if (dialog != null){
            deleteDialog(dialog.lastMessageId!!)

            lastMessage.apply {
                dbQueries.setLastMessage(id, from.toUserEntity(), date, message, status.name,replyMessage?.toReplyMessageEntity(), attachments)
            }

            dialog.apply {
                dbQueries.setDialog(idDialog, appUserId, lastMessage.id, opponent, 0, notifyAboutIt)
            }
        }


    }

    override suspend fun deleteDialog(idDialog: Long) {
        dbQueries.deleteDialog(idDialog)
    }

    override suspend fun deleteAll() {
        dbQueries.clear()
    }


}
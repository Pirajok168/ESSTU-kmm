package ru.esstu.student.messaging.messenger.appeals.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.appeals.datasources.db.AppealTable
import ru.esstu.student.messaging.messenger.appeals.datasources.db.entities.AppealWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.toReplyMessageEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUserEntity

class AppealsCacheDatabase(
    database: EsstuDatabase
): AppealsCacheDao {
    private val dbQueries = database.appealsQueries
    override suspend fun clear() {
        dbQueries.clear()
    }

    override suspend fun setDialog(appUserId: String, conversationPreview: ConversationPreview) {
        conversationPreview.apply {
            dbQueries.setDialog(appUserId, id.toLong(), title, author?.toUserEntity(), lastMessage?.id, notifyAboutIt, unreadMessageCount )
        }
    }

    override suspend fun setLastMessage(message: PreviewLastMessage) {
        message.apply {
            dbQueries.setLastMessage(id, from.toUserEntity(), date, message.message, status.name, replyMessage?.toReplyMessageEntity(), attachments)
        }
    }

    override suspend fun getDialogWithLastMessage(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<AppealWithMessage> {
        fun map(
            messageId: Long,
            fromUser: UserEntity?,
            date: Long,
            message: String,
            status: String,
            replyMessage: ReplyMessageEntity?,
            countAttachments: Int?,
            appUserId: String?,
            idConversation: Long?,
            title: String?,
            author: UserEntity?,
            lastMessageId: Long?,
            notifyAboutIt: Boolean?,
            unread: Int?
        ): AppealWithMessage {
            return AppealWithMessage(
                appeal = AppealTable(
                    appUserId!!, idConversation!!, title!!, author, lastMessageId, notifyAboutIt!!, unread!!
                ),
                lastMessage = LastMessageWithCountAttachments(
                    message = MessageEntity(
                        messageId,
                        fromUser!!,
                        date,
                        message,
                        status,
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
        convId: Int,
        lastMessage: PreviewLastMessage,
        appeal: ConversationPreview?,
        isCreate: Boolean
    ) {
        val dialog = dbQueries.getDialog(convId.toLong(), appUserId).executeAsOneOrNull()
        if (dialog != null){
            dbQueries.deleteDialog(dialog.lastMessageId!!)

            lastMessage.apply {
                dbQueries.setLastMessage(id, from.toUserEntity(), date, message, status.name,replyMessage?.toReplyMessageEntity(), attachments)
            }

            dialog.apply {
                dbQueries.setDialog(appUserId, idConversation, title, author, lastMessage.id, notifyAboutIt, 0 )
            }
        }else{
            if (isCreate){
                setLastMessage(lastMessage)
                setDialog(appUserId, appeal!!)
            }
        }
    }
}
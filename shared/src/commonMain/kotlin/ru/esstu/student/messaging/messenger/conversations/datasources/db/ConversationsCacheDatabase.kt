package ru.esstu.student.messaging.messenger.conversations.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.conversation.datasources.db.ConversationTable
import ru.esstu.student.messaging.messenger.conversations.datasources.db.entities.ConversationWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toReplyMessageEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUserEntity
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage

class ConversationsCacheDatabase(
    database: EsstuDatabase,
): ConversationsCacheDao {
    private val dbQueries = database.conversationQueries
    override suspend fun clear() {
        dbQueries.clear()
    }

    override suspend fun setDialog(appUserId: String, conversation: Conversation) {
        conversation.apply {
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
    ): List<ConversationWithMessage> {
        fun map(
            appUserId: String,
            idConversation: Long,
            title: String,
            author: UserEntity?,
            lastMessageId: Long?,
            notifyAboutIt: Boolean,
            unread: Int,
            messageId: Long?,
            fromUser: UserEntity?,
            date: Long?,
            message: String?,
            status: String?,
            replyMessage: ReplyMessageEntity?,
            countAttachments: Int?
        ): ConversationWithMessage {
            return ConversationWithMessage(
                conversation = ConversationTable(
                   appUserId, idConversation, title, author, lastMessageId, notifyAboutIt, unread
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


}
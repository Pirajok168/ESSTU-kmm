package ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.AttachmentEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachmentsEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.entities.DialogEntity

class CacheDatabase(database: EsstuDatabase) : CacheDao() {
    private val dbQueries = database.dialogsTableQueries
    override suspend fun getMessageWithAttachments(id: Long): MessageWithAttachments {
        fun map(
            messageId: Long,
            fromUser: UserEntity?,
            date: Long?,
            message: String?,
            status: String?,
            replyMessage: ReplyMessageEntity?,
            idAttachment: Int?,
            messageId_: Long?,
            fileUri: String?,
            name: String?,
            ext: String?,
            size: Int?,
            type: String?
        ): MessageWithAttachmentsEntity {
            val messages = MessageEntity(
                id = messageId,
                fromUser!!,
                date!!,
                message,
                status!!,
                replyMessage
            )
            val attachmentsEntity = if(idAttachment == null) null else AttachmentEntity(
                idAttachment,
                messageId,
                fileUri.orEmpty(),
                name,
                ext,
                size!!,
                type
            )
            return MessageWithAttachmentsEntity(
                messages,
                attachmentsEntity
            )
        }

        val query = dbQueries.getMessageWithAttachments(id, ::map).executeAsList()


        val a = query.groupBy {
            it.message.id
        }
        val result = mutableListOf<MessageWithAttachments>()

        a.forEach { (key, value) ->
            val attachments = mutableListOf<AttachmentEntity>()
            var message: MessageEntity? = null

            value.forEach { messageWith ->
                if (result.find { it.message.id == messageWith.message.id } == null) {
                    message = messageWith.message
                }
                if (messageWith.attachments != null) {
                    attachments.add(messageWith.attachments)
                }
            }

            result.add(
                MessageWithAttachments(
                    message = message!!,
                    attachments = attachments,
                )
            )
        }
        return result.first()
    }

    override suspend fun getDialogs(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<DialogEntity> {
        fun map(
            appUserId: String,
            id: String,
            sortOrder: Int,
            lastMessageId: Long?,
            opponent: UserEntity,
            unread: Int,
            notifyAboutIt: Boolean
        ): DialogEntity {
            return DialogEntity(
                appUserId,
                id,
                sortOrder,
                lastMessageId,
                opponent,
                unread,
                notifyAboutIt
            )
        }
        return dbQueries.getDialogs(appUserId, pageSize.toLong(), pageOffset.toLong(), ::map)
            .executeAsList()
    }

    override suspend fun getDialog(appUserId: String, Id: String): DialogEntity? {
        fun map(
            appUserId: String,
            id: String,
            sortOrder: Int,
            lastMessageId: Long?,
            opponent: UserEntity,
            unread: Int,
            notifyAboutIt: Boolean
        ): DialogEntity {
            return DialogEntity(
                appUserId,
                id,
                sortOrder,
                lastMessageId,
                opponent,
                unread,
                notifyAboutIt
            )
        }
        return dbQueries.getDialog(Id, appUserId, ::map).executeAsOneOrNull()
    }

    override suspend fun setDialogs(dialogs: List<DialogEntity>) {
        dialogs.forEach {
            it.apply {
                dbQueries.setDialogs(
                    appUserId,
                    id,
                    sortOrder,
                    lastMessageId,
                    opponent,
                    unread,
                    notifyAboutIt
                )
            }
        }
    }

    override suspend fun setMessage(message: MessageEntity) {
        message.apply {
            dbQueries.setMessage(id, from, date, this.message, status, replyMessage)
        }
    }

    override suspend fun clearMessage(id: Long) {
        dbQueries.clearMessage(id)
    }

    override suspend fun setAttachments(attachments: List<AttachmentEntity>) {
        attachments.forEach {
            it.apply {
                dbQueries.setAttachments(id, messageId, fileUri, name, ext, size, type)
            }
        }
    }

    override suspend fun clearAttachments(messageId: Long) {
        dbQueries.clearAttachmentsById(messageId)
    }

    override suspend fun clearAttachments() {
        dbQueries.clearAttachments()
    }

    override suspend fun updateDialog(appUserId: String, dialogId: String, lastMessageId: Long) {
        dbQueries.updateDialog(lastMessageId, dialogId, appUserId)
    }

    override suspend fun clearDialogs() {
        dbQueries.clearDialogs()
    }

    override suspend fun clearMessages() {
        dbQueries.clearMessages()

    }
}
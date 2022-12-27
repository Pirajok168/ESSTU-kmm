package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelatedEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity

class ErredMessageDatabase(
    database: EsstuDatabase
): ErredMessageDao {
    private val dbQuery = database.messageWithRelatedTAbleQueries

    override suspend fun getCachedFiles(messageId: Long): List<ErredCachedFileEntity> {
        fun map(idCashedFile: Int?,
                messageId: Long,
                name: String,
                ext: String,
                size: Long,
                type: String): ErredCachedFileEntity{
            return ErredCachedFileEntity(
                messageId = messageId,
                source = null,
                name,
                ext,
                size,
                type
            )
        }
        return dbQuery.getCachedFiles(messageId, ::map).executeAsList()
    }

    override suspend fun getReplyMessage(messageId: Long): MessageWithRelated? {
        fun map(
            messageId: Long,
            appUserId: String,
            opponentId: String?,
            fromSend: DialogChatAuthorEntity,
            replyMessageId: Long?,
            date: Long,
            message: String?,
            status: String?,
            idAttachment: Long?,
            messageId_: Long?,
            fileUri: String?,
            LocalFileUri: String?,
            loadProgress: Double?,
            name: String?,
            ext: String?,
            size: Long?,
            type: String?,
            idReplyMessage: Long?,
            fromSendReplyMessage: DialogChatAuthorEntity?,
            dateReplyMessage: Long?,
            messageReplayMessage: String?,
            attachmentsCount: Long?
        ): MessageWithRelatedEntity {
            val messages = DialogChatMessageEntity(
                appUserId = appUserId,
                id = messageId,
                opponentId = opponentId.orEmpty(),
                from = fromSend,
                replyMessageId = replyMessageId,
                date = date,
                message = message.orEmpty(),
                status = status.orEmpty(),
            )

            val attachment = if (idAttachment == null) null else DialogChatAttachmentEntity(
                id = idAttachment.toInt(),
                messageId = messageId_!!,
                fileUri = fileUri.orEmpty(),
                LocalFileUri = LocalFileUri,
                loadProgress = loadProgress?.toFloat(),
                name = name,
                ext = ext,
                size = size!!.toInt(),
                type = type
            )

            return MessageWithRelatedEntity(
                message = messages,
                attachments = attachment,
                reply = if (idReplyMessage == null) null else {
                    DialogChatReplyMessageEntity(
                        id = idReplyMessage,
                        from = fromSendReplyMessage!!,
                        date = dateReplyMessage!!,
                        message = messageReplayMessage.toString(),
                        attachmentsCount = attachmentsCount!!.toInt()
                    )
                }
            )
        }

        val query = dbQuery.getReplyMessage(messageId, ::map).executeAsList()
        val a = query.groupBy {
            it.message.id
        }
        val result = mutableListOf<MessageWithRelated>()

        a.forEach { (key, value) ->
            val attachments = mutableListOf<DialogChatAttachmentEntity>()
            var message: DialogChatMessageEntity? = null
            var reply: DialogChatReplyMessageEntity? = null
            value.forEach { messageWith ->
                if (result.find { it.message.id == messageWith.message.id } == null) {
                    message = messageWith.message
                    reply = messageWith.reply
                }
                if (messageWith.attachments != null) {
                    attachments.add(messageWith.attachments)
                }
            }

            result.add(
                MessageWithRelated(
                    message = message ?: return null,
                    attachments = attachments,
                    reply = reply
                )
            )
        }
        val b = result.firstOrNull()
        b
        return result.firstOrNull()
    }

    override suspend fun getErredMessages(
        appUserId: String,
        dialogId: String
    ): List<ErredMessageEntity> {
        fun map(
             idErredMessage: Long,
             appUserId: String,
             dialogId: String,
             date: Long,
             text: String,
             replyMessageId: Long?
        ): ErredMessageEntity{
            return ErredMessageEntity(idErredMessage, appUserId, dialogId, date, text, replyMessageId)
        }
        return dbQuery.getErredMessages(appUserId, dialogId, ::map).executeAsList()
    }

    override suspend fun removeMessage(id: Long) {
        dbQuery.removeMessage(id)
    }

    override suspend fun addMessage(message: ErredMessageEntity) {
        message.apply {
            dbQuery.addMessage(id, appUserId, dialogId, date, text, replyMessageId)
        }
    }

    override suspend fun addCachedFiles(files: List<ErredCachedFileEntity>) {
        files.forEach {
            it.apply {
                dbQuery.addCachedFiles(id, messageId, name, ext, size, type)
            }
        }
    }


}
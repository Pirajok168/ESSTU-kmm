package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.MessageWithRelatedEntityNew
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredMessageTableNew

class ErredMessageDatabase(
    database: EsstuDatabase
) : ErredMessageDao {
    private val dbQuery = database.erredMessagesQueries

    override suspend fun getCachedFiles(messageId: Long): List<ErredCachedFileTableNew> {
        return dbQuery.getCachedFiles(messageId).executeAsList()
    }

    override suspend fun getReplyMessage(messageId: Long): MessageWithRelatedNew? {
        fun map(
            appUserId: String,
            messageId: Long,
            opponentId: String?,
            fromSend: DialogChatAuthorEntity,
            replyMessageId: Long?,
            date: Long,
            message: String,
            status: String,
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
            messageId__: Long?,
            fromSendReplyMessage: DialogChatAuthorEntity?,
            dateReplyMessage: Long?,
            messageReplayMessage: String?,
            attachmentsCount: Int?
        ): MessageWithRelatedEntityNew {
            val messages = DialogChatMessageTableNew(
                appUserId = appUserId,
                messageId = messageId,
                opponentId = opponentId.orEmpty(),
                fromSend = fromSend,
                replyMessageId = replyMessageId,
                date = date,
                message = message.orEmpty(),
                status = status.orEmpty(),
            )

            val attachment = if (idAttachment == null) null else DialogChatAttachmentTableNew(
                idAttachment = idAttachment,
                messageId = messageId_!!,
                fileUri = fileUri.orEmpty(),
                LocalFileUri = LocalFileUri,
                loadProgress = loadProgress,
                name = name,
                ext = ext,
                size = size!!,
                type = type
            )

            return MessageWithRelatedEntityNew(
                message = messages,
                attachments = attachment,
                reply = if (idReplyMessage == null) null else {
                    DialogChatReplyMessageTableNew(
                        idReplyMessage = idReplyMessage,
                        fromSendReplyMessage = fromSendReplyMessage!!,
                        dateReplyMessage = dateReplyMessage!!,
                        messageReplayMessage = messageReplayMessage.toString(),
                        attachmentsCount = attachmentsCount?.toInt() ?: 0,
                        messageId = messageId
                    )
                }
            )
        }


        val query = dbQuery.getReplyMessage(
            messageId = messageId,
            mapper = ::map
        ).executeAsList()

        val a = query.groupBy {
            it.message.messageId
        }
        val result = mutableListOf<MessageWithRelatedNew>()

        a.forEach { (key, value) ->
            val attachments = mutableListOf<DialogChatAttachmentTableNew>()
            var message: DialogChatMessageTableNew? = null
            var reply: DialogChatReplyMessageTableNew? = null
            value.forEach { messageWith ->
                if (result.find { it.message.messageId == messageWith.message.messageId } == null) {
                    message = messageWith.message
                    reply = messageWith.reply
                }
                if (messageWith.attachments != null) {
                    attachments.add(messageWith.attachments)
                }
            }

            result.add(
                MessageWithRelatedNew(
                    message = message ?: return@forEach ,
                    attachments = attachments,
                    reply = reply
                )
            )
        }
        return result.firstOrNull()
    }

    override suspend fun getErredMessages(
        appUserId: String,
        dialogId: String
    ): List<ErredMessageTableNew> {

        return dbQuery.getErredMessages(appUserId, dialogId).executeAsList()
    }

    override suspend fun removeMessage(id: Long) {
        dbQuery.removeMessage(id)
    }

    override suspend fun addMessage(message: ErredMessageTableNew) {
        message.apply {
            dbQuery.addMessage(idErredMessage, appUserId, dialogId, date, text, replyMessageId)
        }
    }

    override suspend fun addCachedFiles(files: List<ErredCachedFileTableNew>) {
        files.forEach {
            it.apply {
                dbQuery.addCachedFiles(idCahedFile, messageId, name, ext, size, type, source)
            }
        }
    }


}
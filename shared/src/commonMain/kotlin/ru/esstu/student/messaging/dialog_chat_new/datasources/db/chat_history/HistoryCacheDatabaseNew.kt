package ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelatedEntity
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedEntityNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew


class HistoryCacheDatabaseNew(
    database: EsstuDatabase
) : HistoryCacheDaoNew {


    private val dbQuery = database.chatHistoryQueries

    override suspend fun getMessageHistory(
        appUserId: String,
        opponentId: String,
        limit: Int,
        offset: Int
    ): List<MessageWithRelatedNew> {
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


        val query = dbQuery.getMessageHistory(
            appUserId = appUserId,
            opponentId = opponentId,
            limit = limit.toLong(),
            offset = offset.toLong(),
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
                    message = message ?: return emptyList(),
                    attachments = attachments,
                    reply = reply
                )
            )
        }
        return result
    }




    override suspend fun insertMessage(messages: DialogChatMessageTableNew) {
        messages.apply {
            dbQuery.insertMessage(
                messageId,
                opponentId,
                fromSend,
                replyMessageId,
                date,
                message,
                status,
                appUserId
            )
        }
    }

    override suspend fun insertAttachments(attachments: List<DialogChatAttachmentTableNew>) {
        attachments.forEach {
            it.apply {
                dbQuery.insertAttachments(
                    idAttachment,
                    messageId,
                    fileUri,
                    LocalFileUri,
                    name,
                    ext,
                    size,
                    type
                )
            }
        }
    }

    override suspend fun insertReply(reply: DialogChatReplyMessageTableNew) {
        reply.apply {
            dbQuery.insertReply(
                idReplyMessage,
                fromSendReplyMessage,
                dateReplyMessage,
                messageReplayMessage,
                attachmentsCount,
                messageId
            )
        }
    }




}
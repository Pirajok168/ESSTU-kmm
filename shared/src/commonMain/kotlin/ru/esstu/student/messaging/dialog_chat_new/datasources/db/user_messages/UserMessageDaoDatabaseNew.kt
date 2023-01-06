package ru.esstu.student.messaging.dialog_chat_new.datasources.db.user_messages

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedEntityNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable


class UserMessageDatabase(
    database: EsstuDatabase
): UserMessageDaoNew {

    private val dbQuery = database.userMessageTableQueries
    override suspend fun getCachedFiles(
        appUserId: String,
        dialogId: String
    ): List<UserCachedFileTable> {
        return dbQuery.getCachedFiles(appUserId, dialogId).executeAsList()
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

    override suspend fun getUserMessage(
        appUserId: String,
        dialogId: String
    ): UserMessageEntityTable? {
        return dbQuery.getUserMessage(appUserId, dialogId).executeAsOneOrNull()
    }

    override suspend fun removeMessage(appUserId: String, dialogId: String) {
        dbQuery.removeMessage(appUserId, dialogId)
    }

    override suspend fun addMessage(message: UserMessageEntityTable) {
        message.apply {
            dbQuery.addMessage(appUserId, dialogId, text, replyMessageId)
        }
    }

    override suspend fun addCachedFiles(files: List<UserCachedFileTable>) {
        files.forEach {
            it.apply {
                dbQuery.addCachedFiles(idCached, appUserId, dialogId, name, ext, size, type, source)
            }
        }
    }

}
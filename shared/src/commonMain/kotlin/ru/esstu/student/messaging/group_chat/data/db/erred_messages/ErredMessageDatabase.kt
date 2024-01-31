package ru.esstu.student.messaging.group_chat.data.db.erred_messages

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelatedEntity
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredCachedFile
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredMessage

class ErredMessageDatabase(
    database: EsstuDatabase
) : ErredMessageDao {
    private val dbQuery = database.groupChatErredMessageQueries
    override suspend fun getCachedFiles(messageId: Long): List<GroupChatErredCachedFile> {
        return dbQuery.getCachedFiles(messageId).executeAsList()
    }

    override suspend fun getReplyMessage(messageId: Long): MessageGroupChatWithRelated? {
        fun map(
            appUserId: String,
            idGroupChatMessage: Long,
            conversationId: Long,
            fromGroup: GroupChatAuthorEntity,
            replyMessageId: Long?,
            date: Long,
            message: String,
            status: String,
            idAttachment: Long?,
            messageIdAttachment: Long?,
            fileUri: String?,
            LocalFileUri: String?,
            loadProgress: Double?,
            name: String?,
            ext: String?,
            size: Long?,
            type: String?,
            idReplyMessage: Long?,
            messageIdReply: Long?,
            fromSendReplyMessage: GroupChatAuthorEntity?,
            dateReplyMessage: Long?,
            messageReplayMessage: String?,
            attachmentsCount: Int?
        ): MessageGroupChatWithRelatedEntity {
            val messages = GroupChatMessage(
                appUserId = appUserId,
                replyMessageId = replyMessageId,
                date = date,
                message = message.orEmpty(),
                status = status.orEmpty(),
                idGroupChatMessage = idGroupChatMessage,
                conversationId = conversationId,
                fromGroup = fromGroup
            )

            val attachment = if (idAttachment == null) null else GroupChatAttachment(
                idAttachment = idAttachment,
                messageIdAttachment = messageIdAttachment!!,
                fileUri = fileUri.orEmpty(),
                LocalFileUri = LocalFileUri,
                loadProgress = loadProgress,
                name = name,
                ext = ext,
                size = size!!,
                type = type
            )

            return MessageGroupChatWithRelatedEntity(
                message = messages,
                attachments = attachment,
                reply = if (idReplyMessage == null) null else {
                    GroupChatReplyMessage(
                        idReplyMessage = idReplyMessage,
                        fromSendReplyMessage = fromSendReplyMessage!!,
                        dateReplyMessage = dateReplyMessage!!,
                        messageReplayMessage = messageReplayMessage.toString(),
                        attachmentsCount = attachmentsCount?.toInt() ?: 0,
                        messageIdReply = messageIdReply!!
                    )
                }
            )
        }

        val query = dbQuery.getReplyMessage(
            messageId,
            mapper = ::map
        ).executeAsList()

        val a = query.groupBy {
            it.message.idGroupChatMessage
        }
        val result = mutableListOf<MessageGroupChatWithRelated>()
        a.forEach { (key, value) ->
            val attachments = mutableListOf<GroupChatAttachment>()
            var message: GroupChatMessage? = null
            var reply: GroupChatReplyMessage? = null
            value.forEach { messageWith ->
                if (result.find { it.message.idGroupChatMessage == messageWith.message.idGroupChatMessage } == null) {
                    message = messageWith.message
                    reply = messageWith.reply
                }
                if (messageWith.attachments != null) {
                    attachments.add(messageWith.attachments)
                }
            }

            result.add(
                MessageGroupChatWithRelated(
                    message = message ?: return@forEach,
                    attachments = attachments,
                    reply = reply
                )
            )
        }
        return result.firstOrNull()
    }

    override suspend fun getErredMessages(
        appUserId: String,
        convId: Int
    ): List<GroupChatErredMessage> {
        return dbQuery.getErredMessages(appUserId, convId.toLong()).executeAsList()
    }

    override suspend fun removeMessage(id: Long) {
        dbQuery.removeMessage(id)
    }

    override suspend fun addMessage(message: GroupChatErredMessage) {
        message.apply {
            dbQuery.addMessage(idErredMessage, appUserId, convId, date, text, replyMessageId)
        }
    }

    override suspend fun addCachedFiles(files: List<GroupChatErredCachedFile>) {
        files.forEach {
            it.apply {
                dbQuery.addCachedFiles(idCahedFile, messageId, name, ext, size, type, sourceFile)
            }
        }
    }
}
package ru.esstu.student.messaging.group_chat.data.db.chat_history

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelatedEntity
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage

class GroupChatHistoryCacheDatabase(
    database: EsstuDatabase
) : GroupChatHistoryCacheDao {
    private val dbQuery = database.groupChatHistoryQueries
    override suspend fun updateAttachments(
        loadProgress: Float,
        idAttachment: Long,
        localFileUri: String
    ) {
        dbQuery.updateAttachemnt(loadProgress.toDouble(), localFileUri, idAttachment)
    }

    override suspend fun insertMessage(messages: GroupChatMessage) {
        messages.apply {
            dbQuery.insertMessage(
                appUserId,
                idGroupChatMessage,
                conversationId,
                fromGroup,
                replyMessageId,
                date,
                message,
                status
            )
        }
    }

    override suspend fun insertAttachments(attachments: List<GroupChatAttachment>) {
        attachments.forEach {
            it.apply {
                dbQuery.insertAttachments(
                    idAttachment,
                    messageIdAttachment,
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

    override suspend fun insertReply(reply: GroupChatReplyMessage) {
        reply.apply {
            dbQuery.insertReply(
                idReplyMessage,
                fromSendReplyMessage,
                dateReplyMessage,
                messageReplayMessage,
                attachmentsCount,
                messageIdReply
            )
        }
    }

    override suspend fun getMessageHistory(
        appUserId: String,
        conversationId: Long,
        limit: Int,
        offset: Int
    ): List<MessageGroupChatWithRelated> {
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


        val query = dbQuery.getMessageHistory(
            appUserId = appUserId,
            conversationId = conversationId,
            limit = limit.toLong(),
            offset = offset.toLong(),
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
                    message = message ?: return emptyList(),
                    attachments = attachments,
                    reply = reply
                )
            )
        }
        return result
    }

}
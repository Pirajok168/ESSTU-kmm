package ru.esstu.student.messaging.group_chat.data.db.erred_messages

import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.data.db.erred_messages.entities.GroupChatErredMessageWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredCachedFile
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredMessage

interface ErredMessageDao {
    suspend fun getCachedFiles(messageId: Long): List<GroupChatErredCachedFile>

    suspend fun getReplyMessage(messageId: Long): MessageGroupChatWithRelated?

    suspend fun getErredMessages(appUserId: String, convId: Int): List<GroupChatErredMessage>


    suspend fun getErredMessageWithRelated(
        appUserId: String,
        convId: Int
    ): List<GroupChatErredMessageWithRelated> {
        val rawMessages = getErredMessages(appUserId, convId)
        return rawMessages.map { msg ->
            GroupChatErredMessageWithRelated(
                message = msg,
                attachments = getCachedFiles(msg.idErredMessage),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(id: Long)


    suspend fun addMessage(message: GroupChatErredMessage)

    suspend fun addCachedFiles(files: List<GroupChatErredCachedFile>)
}
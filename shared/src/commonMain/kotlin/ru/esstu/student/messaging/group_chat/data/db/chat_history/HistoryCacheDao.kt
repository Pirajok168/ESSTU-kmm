package ru.esstu.student.messaging.group_chat.data.db.chat_history

import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage

interface GroupChatHistoryCacheDao {
    suspend fun updateAttachments(loadProgress: Float, idAttachment: Long, localFileUri: String)

    suspend fun insertMessage(messages: GroupChatMessage)

    suspend fun insertAttachments(attachments: List<GroupChatAttachment>)
    suspend fun insertReply(reply: GroupChatReplyMessage)

    suspend fun insertMessagesWithRelated(messages: List<MessageGroupChatWithRelated>) {
        messages.forEach {
            insertMessage(it.message)
            insertAttachments(it.attachments)
            if (it.reply != null) insertReply(it.reply)
        }
    }


    suspend fun getMessageHistory(
        appUserId: String,
        conversationId: Long,
        limit: Int,
        offset: Int
    ): List<MessageGroupChatWithRelated>
}
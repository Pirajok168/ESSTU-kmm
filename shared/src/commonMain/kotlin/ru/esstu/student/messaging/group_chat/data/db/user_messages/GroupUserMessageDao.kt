package ru.esstu.student.messaging.group_chat.data.db.user_messages

import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.data.db.user_messages.entities.GroupChatUserMessageWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserCachedFileEntity
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserMessage

interface GroupUserMessageDao {
    suspend fun getCachedFiles(appUserId: String, convId: Long): List<GroupChatUserCachedFileEntity>


    suspend fun getReplyMessage(messageId: Long): MessageGroupChatWithRelated?


    suspend fun getUserMessage(appUserId: String, convId: Long): GroupChatUserMessage?


    suspend fun getUserMessageWithRelated(
        appUserId: String,
        dialogId: Long
    ): GroupChatUserMessageWithRelated? {
        val rawMessage = getUserMessage(appUserId, dialogId)
        return rawMessage?.let { msg ->
            GroupChatUserMessageWithRelated(
                message = msg,
                attachments = getCachedFiles(appUserId, dialogId),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(appUserId: String, convId: Long)


    suspend fun addMessage(message: GroupChatUserMessage)


    suspend fun addCachedFiles(files: List<GroupChatUserCachedFileEntity>)


    suspend fun updateUserMessageWithRelated(
        message: GroupChatUserMessage,
        files: List<GroupChatUserCachedFileEntity>
    ) {
        removeMessage(convId = message.conversationId, appUserId = message.appUserId)
        addMessage(message = message)
        addCachedFiles(files = files)
    }
}
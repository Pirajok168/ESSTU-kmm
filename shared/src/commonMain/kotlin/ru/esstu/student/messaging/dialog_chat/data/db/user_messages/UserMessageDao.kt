package ru.esstu.student.messaging.dialog_chat.data.db.user_messages


import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialog_chat.data.db.user_messages.entities.UserMessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable


interface UserMessageDao {

    suspend fun getCachedFiles(appUserId: String, dialogId: String): List<UserCachedFileTable>


    suspend fun getReplyMessage(messageId: Long): MessageWithRelatedNew?


    suspend fun getUserMessage(appUserId: String, dialogId: String): UserMessageEntityTable?


    suspend fun getUserMessageWithRelated(
        appUserId: String,
        dialogId: String
    ): UserMessageWithRelatedNew? {
        val rawMessage = getUserMessage(appUserId, dialogId)
        return rawMessage?.let { msg ->
            UserMessageWithRelatedNew(
                message = msg,
                attachments = getCachedFiles(appUserId, dialogId),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(appUserId: String, dialogId: String)


    suspend fun addMessage(message: UserMessageEntityTable)


    suspend fun addCachedFiles(files: List<UserCachedFileTable>)


    suspend fun updateUserMessageWithRelated(
        message: UserMessageEntityTable,
        files: List<UserCachedFileTable>
    ) {
        removeMessage(dialogId = message.dialogId, appUserId = message.appUserId)
        addMessage(message = message)
        addCachedFiles(files = files)
    }
}
package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.relations.UserMessageWithRelated


interface UserMessageDao {

    suspend fun getCachedFiles(appUserId: String, dialogId: String): List<UserCachedFileEntity>



    suspend fun getReplyMessage(messageId: Long): MessageWithRelated?


    suspend fun getUserMessage(appUserId: String, dialogId: String): UserMessageEntity?


    suspend fun getUserMessageWithRelated(appUserId: String, dialogId: String): UserMessageWithRelated? {
        val rawMessage = getUserMessage(appUserId, dialogId)
        return rawMessage?.let { msg ->
            UserMessageWithRelated(
                message = msg,
                attachments = getCachedFiles(appUserId, dialogId),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(appUserId: String, dialogId: String)


    suspend fun addMessage(message: UserMessageEntity)


    suspend fun addCachedFiles(files: List<UserCachedFileEntity>)


    suspend fun updateUserMessageWithRelated(message: UserMessageEntity, files: List<UserCachedFileEntity>) {
        removeMessage(dialogId = message.dialogId, appUserId = message.appUserId)
        addMessage(message = message)
        addCachedFiles(files = files)
    }
}
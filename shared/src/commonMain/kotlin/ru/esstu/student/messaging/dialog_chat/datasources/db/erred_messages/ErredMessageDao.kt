package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.relations.ErredMessageWithRelated

interface ErredMessageDao {


    suspend fun getCachedFiles(messageId: Long): List<ErredCachedFileEntity>


    suspend fun getReplyMessage(messageId: Long): MessageWithRelated?


    suspend fun getErredMessages(appUserId: String, dialogId: String): List<ErredMessageEntity>


    suspend fun getErredMessageWithRelated(
        appUserId: String,
        dialogId: String
    ): List<ErredMessageWithRelated> {
        val rawMessages = getErredMessages(appUserId, dialogId)
        return rawMessages.map { msg ->
            ErredMessageWithRelated(
                message = msg,
                attachments = getCachedFiles(msg.id),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(id: Long)


    suspend fun addMessage(message: ErredMessageEntity)


    suspend fun addCachedFiles(files: List<ErredCachedFileEntity>)
}
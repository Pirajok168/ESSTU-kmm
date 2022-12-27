package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated


interface HistoryCacheDao {

    suspend fun getOpponent(id: String): DialogChatAuthorEntity?


    suspend fun insert(opponent: DialogChatAuthorEntity)


    suspend fun clear(id: String)

    suspend fun insertMessage(messages: DialogChatMessageEntity)


    suspend fun insertAttachments(attachments: List<DialogChatAttachmentEntity>)


    suspend fun clearAttachments(messageId: Long)


    suspend fun insertReply(reply: DialogChatReplyMessageEntity)


    suspend fun insertMessagesWithRelated(messages: List<MessageWithRelated>) {

        messages.forEach { message ->
            clearAttachments(message.message.id)
        }

        messages.forEach { msg ->
            insertMessage(msg.message)
            insertAttachments(msg.attachments)
            if (msg.reply != null) insertReply(msg.reply)
        }

    }


    suspend fun getMessageHistory(
        appUserId: String,
        opponentId: String,
        limit: Int,
        offset: Int
    ): List<MessageWithRelated>
}
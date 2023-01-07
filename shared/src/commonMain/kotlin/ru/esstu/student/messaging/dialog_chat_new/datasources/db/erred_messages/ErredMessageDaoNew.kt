package ru.esstu.student.messaging.dialog_chat_new.datasources.db.erred_messages

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.relations.ErredMessageWithRelated
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.erred_messages.entities.ErredMessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredMessageTableNew

interface ErredMessageDaoNew {


    suspend fun getCachedFiles(messageId: Long): List<ErredCachedFileTableNew>


    suspend fun getReplyMessage(messageId: Long): MessageWithRelatedNew?


    suspend fun getErredMessages(appUserId: String, dialogId: String): List<ErredMessageTableNew>


    suspend fun getErredMessageWithRelated(
        appUserId: String,
        dialogId: String
    ): List<ErredMessageWithRelatedNew> {
        val rawMessages = getErredMessages(appUserId, dialogId)
        return rawMessages.map { msg ->
            ErredMessageWithRelatedNew(
                message = msg,
                attachments = getCachedFiles(msg.idErredMessage),
                reply = if (msg.replyMessageId != null) getReplyMessage(msg.replyMessageId) else null
            )
        }
    }


    suspend fun removeMessage(id: Long)


    suspend fun addMessage(message: ErredMessageTableNew)


    suspend fun addCachedFiles(files: List<ErredCachedFileTableNew>)
}
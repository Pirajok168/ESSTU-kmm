package ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew


interface HistoryCacheDaoNew {



    suspend fun insertMessage(messages: DialogChatMessageTableNew)

    suspend fun insertAttachments(attachments: List<DialogChatAttachmentTableNew>)
    suspend fun insertReply(reply: DialogChatReplyMessageTableNew)

    suspend fun insertMessagesWithRelated(messages: List<MessageWithRelatedNew>) {
        messages.forEach {
            insertMessage(it.message)
            insertAttachments(it.attachments)
            if (it.reply != null) insertReply(it.reply)
        }
    }


    suspend fun getMessageHistory(
        appUserId: String,
        opponentId: String,
        limit: Int,
        offset: Int
    ): List<MessageWithRelatedNew>
}
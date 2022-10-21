package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated

class HistoryCacheDatabase(
    databaseHistoryCacheFactory: SqlDriver
): HistoryCacheDao {
    override suspend fun insertMessage(messages: DialogChatMessageEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAttachments(attachments: List<DialogChatAttachmentEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAttachments(messageId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun insertReply(reply: DialogChatReplyMessageEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getMessageHistory(
        appUserId: String,
        opponentId: String,
        limit: Int,
        offset: Int
    ): List<MessageWithRelated> {
        TODO("Not yet implemented")
    }

}
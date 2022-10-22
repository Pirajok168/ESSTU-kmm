package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import io.github.aakira.napier.Napier
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ruesstustudentmessagingdialogchatdatasourcesdbchathistory.DialogChatMessageTable
import ruesstustudentmessagingdialogchatdatasourcesdbchathistory.DialogChatReplyMessageTable
import ruesstustudentmessagingdialogchatdatasourcesdbchathistory.GetMessageHistory

interface IDatabaseHistoryCacheFactory {
    val sqlDriver: SqlDriver
}

expect fun databaseHistoryCacheFactory(): IDatabaseHistoryCacheFactory

class HistoryCacheDatabase(
    databaseHistoryCacheFactory: SqlDriver
): HistoryCacheDao {
    private val adapter = object : ColumnAdapter<DialogChatAuthorEntity, String>{
        override fun decode(databaseValue: String): DialogChatAuthorEntity {
            return Json{ }.decodeFromString(databaseValue)
        }

        override fun encode(value: DialogChatAuthorEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val adapter2 = object : ColumnAdapter<DialogChatAuthorEntity, String>{
        override fun decode(databaseValue: String): DialogChatAuthorEntity {
            return Json{ }.decodeFromString(databaseValue)
        }

        override fun encode(value: DialogChatAuthorEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val database = MessageWithRelatedTAble(
        databaseHistoryCacheFactory,
        DialogChatMessageTable.Adapter(adapter),
        DialogChatReplyMessageTable.Adapter(adapter2)
    )
    private val dbQuery = database.messageWithRelatedTAbleQueries

    override suspend fun insertMessage(messages: DialogChatMessageEntity) {
        dbQuery.insertMessage(messageId = messages.id,
            opponentId = messages.opponentId,
            fromSend = messages.from,
            replyMessageId = messages.replyMessageId,
            date = messages.date,
            message = messages.message,
            status = messages.status,
            messages.appUserId
        )
    }

    override suspend fun insertAttachments(attachments: List<DialogChatAttachmentEntity>) {
        attachments.forEach {
            dbQuery.insertAttachments(it.id.toLong(), messageId = it.messageId, fileUri = it.fileUri,
            LocalFileUri = it.LocalFileUri, name = it.name, ext = it.ext, size = it.size.toLong(), type = it.type)
        }
    }

    override suspend fun clearAttachments(messageId: Long) {
        dbQuery.clearAttachments(messageId)
    }

    override suspend fun insertReply(reply: DialogChatReplyMessageEntity) {
        dbQuery.insertReply(id = reply.id, fromSend = reply.from, date = reply.date, reply.message, reply.attachmentsCount.toLong())
    }

    override suspend fun getMessageHistory(
        appUserId: String,
        opponentId: String,
        limit: Int,
        offset: Int
    ): List<MessageWithRelated> {


        val query = dbQuery.getMessageHistory(
            appUserId = appUserId,
            opponentId = opponentId,
            limit = limit.toLong(),
            offset = offset.toLong(),
        ).executeAsList()
        Napier.e(query.toString())
        return emptyList()
    }


}
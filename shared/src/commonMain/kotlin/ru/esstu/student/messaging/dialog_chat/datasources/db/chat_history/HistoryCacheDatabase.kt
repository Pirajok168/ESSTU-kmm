package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import io.github.aakira.napier.Napier
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelatedEntity



class HistoryCacheDatabase(
    database: EsstuDatabase
) : HistoryCacheDao {



    private val dbQuery = database.messageWithRelatedTAbleQueries

    override suspend fun insertMessage(messages: DialogChatMessageEntity) {
        dbQuery.insertMessage(
            messageId = messages.id,
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
            dbQuery.insertAttachments(
                it.id.toLong(),
                messageId = it.messageId,
                fileUri = it.fileUri,
                LocalFileUri = it.LocalFileUri,
                name = it.name,
                ext = it.ext,
                size = it.size.toLong(),
                type = it.type
            )
        }
    }

    override suspend fun clearAttachments(messageId: Long) {
        dbQuery.clearAttachments(messageId)
    }

    override suspend fun insertReply(reply: DialogChatReplyMessageEntity) {
        dbQuery.insertReply(
            idReplyMessage = reply.id,
            fromSendReplyMessage = reply.from,
            dateReplyMessage = reply.date,
            reply.message,
            reply.attachmentsCount.toLong()
        )
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
            mapper = ::map
        ).executeAsList()


       // Napier.e(query.toString())
        return emptyList()
    }

    private fun map(
        messageId: Long,
        appUserId: String,
        opponentId: String?,
        fromSend: DialogChatAuthorEntity,
        replyMessageId: Long?,
        date: Long,
        message: String?,
        status: String?,
        idAttachment: Long?,
        messageId_: Long?,
        fileUri: String?,
        LocalFileUri: String?,
        loadProgress: Double?,
        name: String?,
        ext: String?,
        size: Long?,
        type: String?,
        idReplyMessage: Long?,
        fromSendReplyMessage: DialogChatAuthorEntity?,
        dateReplyMessage: Long?,
        messageReplayMessage: String?,
        attachmentsCount: Long?
    ): MessageWithRelatedEntity {
        val message = DialogChatMessageEntity(
            appUserId = appUserId,
            id = messageId,
            opponentId = opponentId.orEmpty(),
            from = fromSend,
            replyMessageId = replyMessageId,
            date = date,
            message = message.orEmpty(),
            status = status.orEmpty(),
        )

        val attachment = if (idAttachment == null) null else DialogChatAttachmentEntity(
            id = idAttachment.toInt(),
            messageId = messageId_!!,
            fileUri = fileUri.orEmpty(),
            LocalFileUri = LocalFileUri,
            loadProgress = loadProgress?.toFloat(),
            name = name,
            ext = ext,
            size = size!!.toInt(),
            type = type
        )

        return MessageWithRelatedEntity(
            message = message,
            attachments = attachment,
            reply = if (idReplyMessage == null) null else {
                DialogChatReplyMessageEntity(
                    id = idReplyMessage,
                    from = fromSendReplyMessage!!,
                    date = dateReplyMessage!!,
                    message = messageReplayMessage.toString(),
                    attachmentsCount = attachmentsCount!!.toInt()
                )
            }
        )
    }

    override suspend fun getOpponent(id: String): DialogChatAuthorEntity? {
        fun map(
            id: String,
            fitstName: String,
            lastName: String,
            patronymic: String,
            summary: String,
            photo: String
        ): DialogChatAuthorEntity = DialogChatAuthorEntity(
            id, fitstName, lastName, patronymic, summary, photo
        )
        return dbQuery.getOpponent(id, ::map).executeAsOneOrNull()
    }

    override suspend fun insert(opponent: DialogChatAuthorEntity) {
        dbQuery.insert(
            id = opponent.id,
            fitstName = opponent.firstName,
            lastName = opponent.lastName,
            patronymic = opponent.patronymic,
            summary = opponent.summary,
            photo = opponent.photo.orEmpty()
        )
    }

    override suspend fun clear(id: String) {
        dbQuery.clear(id)
    }


}
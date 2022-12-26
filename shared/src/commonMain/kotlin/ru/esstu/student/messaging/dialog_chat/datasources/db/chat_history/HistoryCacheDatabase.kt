package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

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
        fun map(
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
            val messages = DialogChatMessageEntity(
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
                message = messages,
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


        val query = dbQuery.getMessageHistory(
            appUserId = appUserId,
            opponentId = opponentId,
            limit = limit.toLong(),
            offset = offset.toLong(),
            mapper = ::map
        ).executeAsList()

        val a = query.groupBy {
            it.message.id
        }
        val result = mutableListOf<MessageWithRelated>()

        a.forEach { (key, value) ->
            val attachments = mutableListOf<DialogChatAttachmentEntity>()
            var message: DialogChatMessageEntity? = null
            var reply: DialogChatReplyMessageEntity? = null
            value.forEach { messageWith ->
                if (result.find { it.message.id == messageWith.message.id } == null) {
                    message = messageWith.message
                    reply = messageWith.reply
                }
                if (messageWith.attachments != null) {
                    attachments.add(messageWith.attachments)
                }
            }

            result.add(
                MessageWithRelated(
                    message = message ?: return emptyList(),
                    attachments = attachments,
                    reply = reply
                )
            )
        }

        return result
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
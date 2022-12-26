package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.HistoryCacheDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity



class UserMessageDatabase(
    database: EsstuDatabase

): UserMessageDao {

    private val dbQuery = database.userMessageTableQueries
    override suspend fun getCachedFiles(
        appUserId: String,
        dialogId: String
    ): List<UserCachedFileEntity> {

        return dbQuery.getCachedFiles(appUserId, dialogId, ::toUserCachedFileEntity).executeAsList()
    }

    private fun toUserCachedFileEntity(
        id: Long,
        appUserId: String?,
        dialogId: String?,
        name: String?,
        ext: String?,
        size: Long,
        type: String?
    ): UserCachedFileEntity{
        return UserCachedFileEntity(
            appUserId =appUserId.orEmpty(),
            dialogId = dialogId.orEmpty(),
            source = "",
            name = name.orEmpty(),
            ext = ext.orEmpty(),
            size = size,
            type = type.orEmpty(),
            id = id
        )
    }

    override suspend fun getReplyMessage(messageId: Long): MessageWithRelated? {
        TODO()
    }

    override suspend fun getUserMessage(appUserId: String, dialogId: String): UserMessageEntity? {
        return dbQuery.getUserMessage(appUserId, dialogId, ::toUserMessageEntity).executeAsOneOrNull()
    }

    private fun toUserMessageEntity(
        appUserId: String?,
        dialogId: String,
        text: String?,
        replyMessageId: Long?
    ): UserMessageEntity{
        return UserMessageEntity(
            appUserId = appUserId.orEmpty(),
            dialogId = dialogId.orEmpty(),
            text = text.orEmpty(),
            replyMessageId = replyMessageId
        )
    }

    override suspend fun removeMessage(appUserId: String, dialogId: String) {
        dbQuery.removeMessage(appUserId, dialogId)
    }

    override suspend fun addMessage(message: UserMessageEntity) {
        dbQuery.addMessage(message.appUserId, message.dialogId, message.text, message.replyMessageId)
    }

    override suspend fun addCachedFiles(files: List<UserCachedFileEntity>) {
        files.forEach {
            dbQuery.addCachedFiles(it.id?.toLong(), it.appUserId, it.dialogId, it.name, it.ext, it.size, it.type)
        }
    }
}
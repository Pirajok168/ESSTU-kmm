package ru.esstu.student.messaging.messenger.conversations.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations
import ru.esstu.student.messaging.messanger.dialogs.datasources.db.TimstampDialogs
import ru.esstu.student.messaging.messanger.supports.datasources.db.TimstampSupport

class ConversationsTimestampDatabase(
    database: EsstuDatabase
): ConversationsTimestampDao {
    private val dao = database.timestampСonversationsQueries
    override suspend fun getTimestamp(appUserId: String): TimstampConversations? {
        return dao.getTimestamp(appUserId).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimstampConversations) {
        timestamp.apply {
            dao.setTimestamp(appUserId, this.timestamp)
        }
    }
}
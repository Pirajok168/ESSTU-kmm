package ru.esstu.student.messaging.messenger.appeals.datasources.db

import ru.esstu.student.messaging.messanger.appeals.datasources.db.TimstampAppeals
import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations

interface AppealsTimestampDao {
    suspend fun getTimestamp(appUserId: String): TimstampAppeals?


    suspend fun setTimestamp(timestamp: TimstampAppeals)
}
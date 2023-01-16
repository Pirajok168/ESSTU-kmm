package ru.esstu.student.messaging.messenger.appeals.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.appeals.datasources.db.TimstampAppeals

class AppealsTimestampDatabase(
    database: EsstuDatabase
): AppealsTimestampDao {
    private val dao = database.timestampAppealsQueries
    override suspend fun getTimestamp(appUserId: String): TimstampAppeals? {
        return dao.getTimestamp(appUserId).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimstampAppeals) {
        timestamp.apply {
            dao.setTimestamp(appUserId, this.timestamp)
        }
    }
}
package ru.esstu.student.messaging.messenger.supports.datasource.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.supports.datasources.db.TimstampSupport

class SupportsTimestampDatabase(
    database: EsstuDatabase
): SupportsTimestampDao {
    private val dao = database.timestampSupportQueries
    override suspend fun getTimestamp(appUserId: String): TimstampSupport? {
        return dao.getTimestamp(appUserId).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimstampSupport) {
        timestamp.apply {
            dao.setTimestamp(appUserId, this.timestamp)
        }
    }
}
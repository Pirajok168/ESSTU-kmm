package ru.esstu.student.messaging.messenger.datasources.db.timestamp

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messenger.datasources.db.timestamp.entities.TimestampEntity

class TimestampDatabase(
    database: EsstuDatabase
): TimestampDao {
    private val dao = database.timestampDatabaseQueries
    override suspend fun getTimestamp(appUserId: String): TimestampEntity? {
        fun map(appUserId: String, timestamp: Long): TimestampEntity{
            return TimestampEntity(appUserId, timestamp)
        }
        return dao.getTimestamp(appUserId,::map).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimestampEntity) {
        timestamp.apply {
            dao.setTimestamp(appUserId, this.timestamp)
        }
    }

}
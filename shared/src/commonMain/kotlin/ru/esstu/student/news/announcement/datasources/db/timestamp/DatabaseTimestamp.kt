package ru.esstu.student.news.announcement.datasources.db.timestamp

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.news.announcement.datasources.db.TimestampDatabase
import ru.esstu.student.news.announcement.datasources.db.timestamp.entities.TimestampEntity

internal class DatabaseTimestamp(databaseTimestampFactory: SqlDriver): TimestampDao {

    private val database = TimestampDatabase(databaseTimestampFactory)
    private val dbQuery = database.timestampDatabaseQueries
    override suspend fun getTimestamp(appUserId: String): TimestampEntity? {
        return dbQuery.getTimestamp(appUserId, ::map).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimestampEntity) {
        dbQuery.setTimestamp(appUserId = timestamp.appUserId, timestamp = timestamp.timestamp)
    }
}
private fun map(
    appUserId: String,
    timestamp: Long
): TimestampEntity{
    return TimestampEntity(
        appUserId, timestamp
    )
}
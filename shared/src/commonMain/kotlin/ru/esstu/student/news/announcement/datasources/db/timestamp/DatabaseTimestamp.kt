package ru.esstu.student.news.announcement.datasources.db.timestamp

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.EsstuDatabase

import ru.esstu.student.news.announcement.datasources.db.timestamp.entities.TimestampEntity

internal class DatabaseTimestamp(database: EsstuDatabase): TimestampDao {


    private val dbQuery = database.timestampDatabaseQueries
    override suspend fun getTimestamp(appUserId: String): TimestampEntity? {
        val a =  dbQuery.getTimestamp(appUserId, ::map).executeAsList()
        a
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
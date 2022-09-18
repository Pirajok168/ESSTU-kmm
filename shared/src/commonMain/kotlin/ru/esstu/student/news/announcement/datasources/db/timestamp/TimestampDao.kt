package ru.esstu.student.news.announcement.datasources.db.timestamp

import ru.esstu.student.news.announcement.datasources.db.timestamp.entities.TimestampEntity

interface TimestampDao {
    suspend fun getTimestamp(appUserId: String): TimestampEntity?

    suspend fun setTimestamp(timestamp:TimestampEntity)
}
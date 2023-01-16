package ru.esstu.student.messaging.messenger.supports.datasource.db

import ru.esstu.student.messaging.messanger.supports.datasources.db.TimstampSupport

interface SupportsTimestampDao {
    suspend fun getTimestamp(appUserId: String): TimstampSupport?


    suspend fun setTimestamp(timestamp: TimstampSupport)
}
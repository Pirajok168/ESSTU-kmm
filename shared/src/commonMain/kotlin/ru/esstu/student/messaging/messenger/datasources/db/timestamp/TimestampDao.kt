package ru.esstu.student.messaging.messenger.datasources.db.timestamp


import ru.esstu.student.messaging.messenger.datasources.db.timestamp.entities.TimestampEntity


interface TimestampDao {

    suspend fun getTimestamp(appUserId: String): TimestampEntity?


    suspend fun setTimestamp(timestamp:TimestampEntity)
}
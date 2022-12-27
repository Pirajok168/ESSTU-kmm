package ru.esstu.student.messaging.messenger.datasources.db.timestamp

import ru.esstu.student.messaging.messenger.datasources.db.timestamp.entities.TimestampEntity


fun TimestampEntity.toTimeStamp() = timestamp

fun Long.toTimeStampEntity(appUserId:String) = TimestampEntity(
    appUserId = appUserId,
    timestamp = this
)
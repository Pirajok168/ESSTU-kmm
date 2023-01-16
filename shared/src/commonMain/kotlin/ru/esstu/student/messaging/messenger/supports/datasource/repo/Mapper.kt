package ru.esstu.student.messaging.messenger.supports.datasource.repo

import ru.esstu.student.messaging.messanger.supports.datasources.db.TimstampSupport


fun TimstampSupport.toTimeStamp() = timestamp

fun Long.toTimeStampEntity(appUserId:String) = TimstampSupport(
    appUserId = appUserId,
    timestamp = this
)
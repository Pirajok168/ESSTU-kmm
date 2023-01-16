package ru.esstu.student.messaging.messenger.dialogs.datasources.db

import ru.esstu.student.messaging.messanger.dialogs.datasources.db.TimstampDialogs

interface DialogsTimestampDao {
    suspend fun getTimestamp(appUserId: String): TimstampDialogs?


    suspend fun setTimestamp(timestamp: TimstampDialogs)
}
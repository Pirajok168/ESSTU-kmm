package ru.esstu.student.messaging.messenger.dialogs.datasources.db

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.messanger.dialogs.datasources.db.TimstampDialogs
import ru.esstu.student.messaging.messanger.supports.datasources.db.TimstampSupport

class DialogsTimestampDatabase(
    database: EsstuDatabase
): DialogsTimestampDao {
    private val dao = database.timestampDialogsQueries
    override suspend fun getTimestamp(appUserId: String): TimstampDialogs? {
        return dao.getTimestamp(appUserId).executeAsOneOrNull()
    }

    override suspend fun setTimestamp(timestamp: TimstampDialogs) {
        timestamp.apply {
            dao.setTimestamp(appUserId, this.timestamp)
        }
    }
}
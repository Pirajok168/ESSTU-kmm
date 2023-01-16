package ru.esstu.student.messaging.messenger.appeals.datasources.db.entities

import ru.esstu.student.messaging.messanger.appeals.datasources.db.AppealTable
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments

data class AppealWithMessage(
    val appeal: AppealTable,
    val lastMessage: LastMessageWithCountAttachments
)

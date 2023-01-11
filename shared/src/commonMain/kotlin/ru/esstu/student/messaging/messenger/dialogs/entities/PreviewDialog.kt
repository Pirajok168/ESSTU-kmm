package ru.esstu.student.messaging.messenger.dialogs.entities

import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

data class PreviewDialog(
    val id: String,
    val opponent: Sender,
    val lastMessage: PreviewLastMessage?,
    val notifyAboutIt: Boolean,
    val unreadMessageCount: Int
)
package ru.esstu.student.messaging.messenger.dialogs.entities

import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.Sender

data class PreviewDialog(
    val id: String,
    val opponent: Sender,
    val lastMessage: PreviewLastMessage?,
    val notifyAboutIt: Boolean,
    val unreadMessageCount: Int
)
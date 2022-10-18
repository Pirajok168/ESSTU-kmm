package ru.esstu.student.messaging.messenger.appeals.entities

import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.Sender

data class Appeals(
    val id: Int,
    val title: String,
    val author: Sender,
    val lastMessage: Message?,
    val notifyAboutIt: Boolean,
    val unreadMessageCount: Int
)
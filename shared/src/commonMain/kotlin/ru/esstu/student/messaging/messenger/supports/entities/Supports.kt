package ru.esstu.student.messaging.messenger.supports.entities

import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.User

data class Supports(
    val id: Int,
    val title: String,
    val author: User,
    val lastMessage: Message?,
    val notifyAboutIt: Boolean,
    val unreadMessageCount: Int
)
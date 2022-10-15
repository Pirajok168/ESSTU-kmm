package ru.esstu.student.messaging.group_chat.entities

import ru.esstu.student.messaging.entities.User

data class Conversation(
    val id: Int,
    val title: String,
    val author: User,
    val participants:List<User>,
    val notifyAboutIt: Boolean,
)

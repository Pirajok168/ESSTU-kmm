package ru.esstu.student.messaging.group_chat.entities

import ru.esstu.student.messaging.entities.Sender

data class Conversation(
    val id: Int,
    val title: String,
    val author: Sender,
    val participants:List<Sender>,
    val notifyAboutIt: Boolean,
)

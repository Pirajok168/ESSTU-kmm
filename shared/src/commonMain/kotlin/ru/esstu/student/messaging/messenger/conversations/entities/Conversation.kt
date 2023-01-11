package ru.esstu.student.messaging.messenger.conversations.entities


import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

data class Conversation(
    val id: Int,
    val title: String,
    val author: Sender?,
    val lastMessage: PreviewLastMessage?,
    val notifyAboutIt: Boolean,
    val unreadMessageCount: Int
)

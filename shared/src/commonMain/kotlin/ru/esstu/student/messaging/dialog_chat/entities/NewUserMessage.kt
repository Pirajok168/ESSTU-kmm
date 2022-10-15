package ru.esstu.student.messaging.dialog_chat.entities

import ru.esstu.student.messaging.entities.Message


data class NewUserMessage(
    val attachments: List<CachedFile> = emptyList(),
    val text: String = "",
    val replyMessage: Message? = null
)
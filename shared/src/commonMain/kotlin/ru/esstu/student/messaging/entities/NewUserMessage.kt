package ru.esstu.student.messaging.entities


data class NewUserMessage(
    val attachments: List<CachedFile> = emptyList(),
    val text: String = "",
    val replyMessage: Message? = null
)
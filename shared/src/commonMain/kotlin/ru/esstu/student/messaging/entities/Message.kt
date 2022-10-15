package ru.esstu.student.messaging.entities


import ru.esstu.student.messaging.entities.Attachment


data class Message(
    val id: Long,
    val from: User,
    val date: Long,
    val message: String,
    val replyMessage: ReplyMessage? = null,
    val status: DeliveryStatus,
    val attachments: List<Attachment> = emptyList()
)


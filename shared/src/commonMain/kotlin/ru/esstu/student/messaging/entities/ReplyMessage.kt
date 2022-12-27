package ru.esstu.student.messaging.entities

@kotlinx.serialization.Serializable
data class ReplyMessage(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val attachmentsCount: Int
)
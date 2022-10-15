package ru.esstu.student.messaging.entities



data class ReplyMessage(
    val id: Long,
    val from: User,
    val date: Long,
    val message: String,
    val attachmentsCount: Int
)
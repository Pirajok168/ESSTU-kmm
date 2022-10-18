package ru.esstu.student.messaging.entities


import com.soywiz.klock.DateTime


data class Message(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val replyMessage: ReplyMessage? = null,
    val status: DeliveryStatus,
    val attachments: List<MessageAttachment> = emptyList()
){
    val formatDate: DateTime = DateTime(date)
}



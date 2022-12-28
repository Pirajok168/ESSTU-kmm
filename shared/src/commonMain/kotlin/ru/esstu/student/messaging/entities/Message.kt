package ru.esstu.student.messaging.entities


import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz


data class Message(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val replyMessage: ReplyMessage? = null,
    val status: DeliveryStatus,
    val attachments: Int
) {
    val formatDate: DateTime = DateTime(date)
}



package ru.esstu.student.messaging.messenger.datasources.entities


import com.soywiz.klock.DateTime
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender


data class PreviewLastMessage(
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



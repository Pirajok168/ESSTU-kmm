package ru.esstu.student.messaging.entities
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import ru.esstu.domain.utill.workingDate.toLocalDateTime


data class Message(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val replyMessage: ReplyMessage? = null,
    val status: DeliveryStatus,
    val attachments: List<MessageAttachment>
) {
    val formatDate: LocalDateTime = Instant.fromEpochMilliseconds(date).toLocalDateTime()
}



package ru.esstu.student.messaging.messenger.datasources.entities


import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender


@Serializable
data class PreviewLastMessage(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val replyMessage: ReplyMessage? = null,
    val status: DeliveryStatus,
    val attachments: Int
) {
    val formatDate: LocalDateTime = Instant.fromEpochMilliseconds(date).toLocalDateTime()
}



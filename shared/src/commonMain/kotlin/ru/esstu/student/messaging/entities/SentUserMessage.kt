package ru.esstu.student.messaging.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import kotlin.random.Random


data class SentUserMessage(
    val id: Long = Random.nextLong(),
    val attachments: List<CachedFile> = emptyList(),
    val text: String = "",
    val replyMessage: Message? = null,
    val date: Long = Clock.System.now().toEpochMilliseconds(),
    val status: DeliveryStatus = DeliveryStatus.SENT
) {
    val formatDate: LocalDateTime = Instant.fromEpochMilliseconds(date).toLocalDateTime()
}


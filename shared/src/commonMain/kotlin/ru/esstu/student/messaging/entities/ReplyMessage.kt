package ru.esstu.student.messaging.entities

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.esstu.domain.utill.workingDate.toLocalDateTime


@Serializable
data class ReplyMessage(
    val id: Long,
    val from: Sender,
    val date: Long,
    val message: String,
    val attachmentsCount: Int
){
    val dateTim: LocalDateTime = Instant.fromEpochMilliseconds(date).toLocalDateTime()
}
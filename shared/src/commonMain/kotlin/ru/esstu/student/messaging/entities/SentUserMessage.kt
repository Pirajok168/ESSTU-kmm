package ru.esstu.student.messaging.entities

import com.soywiz.klock.DateTime
import kotlin.random.Random


data class SentUserMessage(
    val id: Long = Random.nextLong(),
    val attachments: List<CachedFile> = emptyList(),
    val text: String = "",
    val replyMessage: Message? = null,
    val date: Long = DateTime.nowUnixLong(),
    val status: DeliveryStatus = DeliveryStatus.SENT
) {
    val formatDate: DateTime = DateTime(date)
}


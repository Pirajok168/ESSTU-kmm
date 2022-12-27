package ru.esstu.student.messaging.dialog_chat.entities

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
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


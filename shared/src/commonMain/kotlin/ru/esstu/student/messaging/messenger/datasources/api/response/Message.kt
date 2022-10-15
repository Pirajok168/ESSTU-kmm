package ru.esstu.student.messaging.messenger.datasources.api.response

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val attachments: List<Attachment>,
    val context: String?,
    val date: Long,
    val flags: Int,
    val from: String,
    val id: Long,
    val message: String?,
    val peerId: String,
    val replyToMsgId: Long?,
    val type: Int,
    val views: Int
)

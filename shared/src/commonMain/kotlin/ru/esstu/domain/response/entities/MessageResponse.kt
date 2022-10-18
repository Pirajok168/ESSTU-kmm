package ru.esstu.domain.response.entities

import kotlinx.serialization.Serializable


@Serializable
data class MessageResponse(
    val attachments: List<AttachmentResponse>,
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

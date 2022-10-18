package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachment

@Serializable
data class ChatMessage(
    val attachments: List<FileAttachment>,
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
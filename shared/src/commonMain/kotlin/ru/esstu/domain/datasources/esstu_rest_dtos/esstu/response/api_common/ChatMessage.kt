package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachment


data class ChatMessage(
    val attachments: List<FileAttachment>,
    val context: Any,
    val date: Long,
    val flags: Int,
    val from: String,
    val id: Int,
    val message: String?,
    val peerId: String,
    val replyToMsgId: Int?,
    val type: Int,
    val views: Int
)
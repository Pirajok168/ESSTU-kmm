package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachment

@Serializable
data class ChatMessageResponse(
    val attachments: List<FileAttachment>,
    val date: Long,
    val id: Long
)
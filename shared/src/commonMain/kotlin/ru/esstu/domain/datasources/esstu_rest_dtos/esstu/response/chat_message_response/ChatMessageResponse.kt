package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_message_response

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachment


data class ChatMessageResponse(
    val attachments: List<FileAttachment>,
    val date: Long,
    val id: Long
)
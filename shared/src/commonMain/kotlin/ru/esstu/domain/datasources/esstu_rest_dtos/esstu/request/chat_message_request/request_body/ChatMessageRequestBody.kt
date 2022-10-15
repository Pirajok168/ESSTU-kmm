package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.IPeer


data class ChatMessageRequestBody(
    val message: String,
    val peer: IPeer,
    val replyToId: Int? = null
)
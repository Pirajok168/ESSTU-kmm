package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.IPeer

@Serializable
data class ChatMessageRequestBody(
    val message: String,
    val peer: IPeer_,
    val replyToId: Int? = null
)
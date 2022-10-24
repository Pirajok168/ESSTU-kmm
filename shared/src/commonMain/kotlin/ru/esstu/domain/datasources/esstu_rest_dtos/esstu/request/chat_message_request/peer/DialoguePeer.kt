package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_

@Serializable
data class DialoguePeer(
    val userId: String
): IPeer()
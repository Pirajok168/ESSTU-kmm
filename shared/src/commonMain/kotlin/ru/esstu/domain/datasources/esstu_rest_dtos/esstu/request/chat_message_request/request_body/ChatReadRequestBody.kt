package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.IPeer


@Serializable
sealed class IPeer_{
    @Serializable
    data class DialoguePeer(val userId: String): IPeer_()

    @Serializable
    data class ConversionPeer(
        val chatId: Int
    ): IPeer_()

}

@Serializable
data class ChatRequestBody(
    val peer: IPeer_
)

@Serializable
data class ChatReadRequestBody (
    val maxId:Int,
    val peer: IPeer_
)
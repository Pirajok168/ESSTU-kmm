package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.IPeer

data class ChatReadRequestBody (
    val maxId:Int,
    val peer: IPeer
)
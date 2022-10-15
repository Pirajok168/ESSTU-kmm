package ru.esstu.student.messaging.dialog_chat.datasources.api.request

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.IPeer


data class ReadRequest(
    val maxId:Int,
    val peer: IPeer
)

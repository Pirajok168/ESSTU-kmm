package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.request.message


data class MessageRequest(
    val dialogId: Int,
    val role: String,

    val senderId: Int,
    val sender: String,

    val message: String,
    val date: String
)
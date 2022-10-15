package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.create_chat_request


data class CreateChatRequestBody(
    val message: String?,
    val name: String,
    val type: String,
    val users: List<String>
)
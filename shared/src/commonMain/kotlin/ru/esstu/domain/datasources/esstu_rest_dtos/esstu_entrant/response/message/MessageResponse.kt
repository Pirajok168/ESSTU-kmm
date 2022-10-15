package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message


data class MessageResponse(
    val id: Int,
    val dialogId: Int,
    val senderId: Int?,
    val role: String?,
    val date: String,
    val message: String?,
    val sender: String?,
    val attachments: Boolean?,
    val filesGuid: String?,
    val fileName: String?
)
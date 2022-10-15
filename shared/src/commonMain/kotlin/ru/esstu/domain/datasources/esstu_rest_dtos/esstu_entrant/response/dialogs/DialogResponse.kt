package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.dialogs


data class DialogResponse(
    val entrantId: Int,
    val id: Int,
    val lastMessage: Int,
    val moderatorRole: String
)
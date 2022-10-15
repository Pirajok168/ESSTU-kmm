package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.request.dialog


data class DialogRequest(
    val entrantId: Int,
    val moderatorRole: String
)
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes


data class ParticipantGroup(
    val date: Long,
    val flags: Int,
    val id: String,
    val invitedBy: String,
    val type: String
)
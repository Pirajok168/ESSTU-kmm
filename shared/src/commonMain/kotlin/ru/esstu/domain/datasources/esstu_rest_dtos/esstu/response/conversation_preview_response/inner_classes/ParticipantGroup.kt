package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantGroup(
    val id: String, // id
    val type: String, // тип
    val invitedBy: String, // кем приглашен
    val date: Long, // дата приглашения
    val flags: Int // 0x1-создатель 0x2-админ
)
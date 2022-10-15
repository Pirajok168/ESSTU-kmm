package ru.esstu.student.messaging.group_chat.datasources.api.response

import kotlinx.serialization.Serializable


@Serializable
data class ParticipantResponse(
    val id: String, // id
    val type: String, // тип
    val invitedBy: String, // кем приглашен
    val date: Long, // дата приглашения
    val flags: Int // 0x1-создатель 0x2-админ
)

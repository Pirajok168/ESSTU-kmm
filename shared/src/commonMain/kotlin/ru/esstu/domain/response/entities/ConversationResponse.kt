package ru.esstu.domain.response.entities

import kotlinx.serialization.Serializable

@Suppress("SpellCheckingInspection")
@Serializable
data class ConversationResponse(
    val id: Int,
    val type: String,
    val name: String,
    val description: String?,
    val context: String?,
    val participantsCount: Int,
    val date: Long,
    val creatorId:String,
    val flags: Int
)
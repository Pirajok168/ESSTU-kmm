package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes

import kotlinx.serialization.Serializable

@Serializable
data class ConversationPreview(
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
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes

import kotlinx.serialization.Serializable

@Serializable
data class ConversationUserPreview(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val information: String?,
    val photo: String?,
    val sex: String?
)
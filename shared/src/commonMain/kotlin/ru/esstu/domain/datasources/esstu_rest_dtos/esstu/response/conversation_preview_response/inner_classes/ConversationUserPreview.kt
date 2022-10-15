package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes

data class ConversationUserPreview(
    val firstName: String,
    val hasSubgroups: Boolean,
    val id: String,
    val information: String,
    val lastName: String,
    val name: String,
    val patronymic: String,
    val photo: String,
    val sex: String,
    val shortName: Any,
    val usersCount: Int
)
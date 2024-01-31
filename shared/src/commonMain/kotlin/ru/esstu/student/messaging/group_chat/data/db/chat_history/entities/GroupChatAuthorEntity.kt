package ru.esstu.student.messaging.group_chat.data.db.chat_history.entities


@kotlinx.serialization.Serializable
data class GroupChatAuthorEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?
)
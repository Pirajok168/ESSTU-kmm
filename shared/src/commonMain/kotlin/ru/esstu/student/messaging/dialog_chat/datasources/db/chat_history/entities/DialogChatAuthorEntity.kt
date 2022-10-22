package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities

import kotlinx.serialization.Serializable


@Serializable
data class DialogChatAuthorEntity(

    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?
)
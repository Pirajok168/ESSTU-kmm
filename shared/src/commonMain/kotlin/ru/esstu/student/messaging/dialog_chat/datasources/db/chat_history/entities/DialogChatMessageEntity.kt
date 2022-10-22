package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities

import kotlinx.serialization.Serializable


@Serializable
data class DialogChatMessageEntity(
    val appUserId:String, //обычно внешний ключ, но здесь просто поле, относительно которого выбирается кэш только для текущего аккаунта


    val id: Long,
    val opponentId: String,

    val from: DialogChatAuthorEntity,
    val replyMessageId: Long?,

    val date: Long,
    val message: String,
    val status: String
)
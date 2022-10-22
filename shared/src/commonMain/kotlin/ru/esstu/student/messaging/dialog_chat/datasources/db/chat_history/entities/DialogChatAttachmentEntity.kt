package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities

import kotlinx.serialization.Serializable


@Serializable
data class DialogChatAttachmentEntity(

    val id: Int,
    val messageId: Long,
    val fileUri: String,
    val LocalFileUri: String?,
    val loadProgress: Float?,
    val name: String?,
    val ext: String?,
    val size: Int,
    val type: String?,
)
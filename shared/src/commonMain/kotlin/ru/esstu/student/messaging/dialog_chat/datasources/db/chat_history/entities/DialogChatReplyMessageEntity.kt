package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities


data class DialogChatReplyMessageEntity(

    val id: Long,


    val from: DialogChatAuthorEntity,
    val date: Long,
    val message: String?,
    val attachmentsCount: Int
)
package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities




data class UserMessageEntity(
    val appUserId:String,
    val dialogId:String,

    val text:String,
    val replyMessageId:Long?
)

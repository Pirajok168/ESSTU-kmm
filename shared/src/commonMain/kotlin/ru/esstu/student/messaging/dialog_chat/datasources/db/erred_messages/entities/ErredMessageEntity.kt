package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities


data class ErredMessageEntity(

    val id:Long,

    val appUserId:String,
    val dialogId:String,
    val date:Long,

    val text:String,
    val replyMessageId:Long?
)

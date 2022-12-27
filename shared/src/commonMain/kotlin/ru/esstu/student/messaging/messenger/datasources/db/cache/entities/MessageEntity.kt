package ru.esstu.student.messaging.messenger.datasources.db.cache.entities


data class MessageEntity(

    val id: Long,

    val from: UserEntity,
    val date: Long,
    val message: String?,
    val status:String,

    val replyMessage: ReplyMessageEntity?,
)


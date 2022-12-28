package ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities


import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity


data class DialogEntity(
    val appUserId: String,
    val id: String,

    val sortOrder:Int,

    val lastMessageId:Long?,

    val opponent: UserEntity,

    val unread: Int = 0,
    val notifyAboutIt: Boolean,
)
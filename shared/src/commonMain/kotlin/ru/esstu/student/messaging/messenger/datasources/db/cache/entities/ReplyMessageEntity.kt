package ru.esstu.student.messaging.messenger.datasources.db.cache.entities


@kotlinx.serialization.Serializable
data class ReplyMessageEntity(
    val id: Long,

    val from: UserEntity,
    val date: Long,
    val message: String?,
    val attachmentsCount: Int
)
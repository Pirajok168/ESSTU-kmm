package ru.esstu.student.messaging.messenger.datasources.db.cache.entities


data class AttachmentEntity(

    val id: Int,
    val messageId: Long,
    val fileUri: String,
    val name: String?,
    val ext: String?,
    val size: Int,
    val type: String?,
)
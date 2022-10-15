package ru.esstu.student.messaging.messenger.datasources.api.response

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: Int,
    val fileCode: String,
    val fileName: String,
    val fileSize: Int,
    val type: String
)
package ru.esstu.domain.response.entities

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentResponse(
    val id: Int,
    val fileCode: String,
    val fileName: String,
    val fileSize: Int,
    val type: String
)
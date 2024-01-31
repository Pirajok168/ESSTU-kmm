package ru.esstu.features.news.announcement.data.db.entities

import kotlinx.serialization.Serializable


@Serializable
data class NewsAttachmentEntity(
    val idAttachment: Int,
    val newsId: Long,
    val fileUri: String,
    val name: String?,
    val ext: String?,
    val localFileUri: String?,
    val loadProgress: Float?,
    val size: Int,
    val type: String?,
)
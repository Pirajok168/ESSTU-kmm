package ru.esstu.features.news.announcement.data.db.entities

import kotlinx.serialization.Serializable


@Serializable
data class NewsEntity(
    val id: Long,
    val from: UserEntity,

    val date: Long,
    val title: String,
    val message: String,
)
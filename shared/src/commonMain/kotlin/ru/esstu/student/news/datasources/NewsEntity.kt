package ru.esstu.student.news.datasources

import kotlinx.serialization.Serializable


@Serializable
data class NewsEntity(
    val id: Long,
    val from: UserEntity,

    val date: Long,
    val title: String,
    val message: String,
)
package ru.esstu.student.news.announcement.db.announcement.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id:String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?
)
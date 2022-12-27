package ru.esstu.student.messaging.messenger.datasources.db.cache.entities

data class UserEntity (
    val id:String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?
)

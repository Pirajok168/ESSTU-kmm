package ru.esstu.domain.response.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse (
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val information: String?,
    val photo: String?,
    val sex: String?
)

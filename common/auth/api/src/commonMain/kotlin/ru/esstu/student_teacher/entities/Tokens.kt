package ru.esstu.student_teacher.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Tokens(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val scope: String,
    @SerialName("token_type")
    val tokenType: String,
    val userId: String,
    val userType: String
)

package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.auth_response


import kotlinx.serialization.SerialName

data class UserAuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
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
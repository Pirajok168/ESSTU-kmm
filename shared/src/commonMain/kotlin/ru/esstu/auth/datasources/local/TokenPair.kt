package ru.esstu.auth.datasources.local

import kotlinx.serialization.Serializable


@Serializable
data class TokenPair(
    val tokenType:String,
    val accessToken: String,
    val refreshToken: String,
    val userType: String,
    val expiresIn: Long?
)
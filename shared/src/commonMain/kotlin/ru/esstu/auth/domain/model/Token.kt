package ru.esstu.auth.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Token(
    val type: String,
    val access: String,
    val refresh: String,
    val owner: TokenOwners,
    val expiresIn: Long?
)

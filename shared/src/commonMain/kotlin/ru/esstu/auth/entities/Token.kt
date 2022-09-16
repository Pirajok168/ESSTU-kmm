package ru.esstu.auth.entities

import kotlinx.serialization.Serializable
import ru.esstu.auth.datasources.entities.TokenOwners


@Serializable
data class Token(
    val type:String,
    val access: String,
    val refresh: String,
    val owner: TokenOwners
)

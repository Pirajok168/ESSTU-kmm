package ru.esstu.domain.modules.account.datasources.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val notifySettings: Boolean,
    @SerialName("user")
    val user: User
)

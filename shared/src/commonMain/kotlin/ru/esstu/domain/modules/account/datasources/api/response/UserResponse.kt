package ru.esstu.domain.modules.account.datasources.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview

@Serializable
data class UserResponse(
    val notifySettings: Boolean,
    @SerialName("user")
    val user: UserPreview
)

package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response



import kotlinx.serialization.SerialName
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview

data class UserPreviewResponse(
    val notifySettings: Boolean,
    @SerialName("user")
    val userPreview: UserPreview
)
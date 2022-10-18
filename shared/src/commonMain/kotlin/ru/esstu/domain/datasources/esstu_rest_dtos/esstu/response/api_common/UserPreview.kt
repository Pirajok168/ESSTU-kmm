package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common

import kotlinx.serialization.Serializable

@Serializable
data class UserPreview(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val information: String?,
    val photo: String?,
    val sex: String?
)
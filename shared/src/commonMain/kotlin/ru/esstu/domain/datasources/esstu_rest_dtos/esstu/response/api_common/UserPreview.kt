package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common

import kotlinx.serialization.Serializable

@Serializable
data class UserPreview(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String?= null,
    val patronymic: String?= null,
    val information: String?= null,
    val photo: String?= null,
    val sex: String?= null
)
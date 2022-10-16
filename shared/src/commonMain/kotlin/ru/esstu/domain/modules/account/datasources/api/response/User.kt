package ru.esstu.domain.modules.account.datasources.api.response

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val information: String?,
    val photo: String?,
    val sex: String?
)
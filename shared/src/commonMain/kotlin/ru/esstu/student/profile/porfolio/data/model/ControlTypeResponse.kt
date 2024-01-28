package ru.esstu.student.profile.porfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ControlTypeResponse(
    val code: String?,
    val name: String?,
    val shortName: String?
)

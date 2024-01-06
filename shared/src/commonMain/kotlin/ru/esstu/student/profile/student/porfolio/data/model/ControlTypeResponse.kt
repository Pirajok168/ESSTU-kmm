package ru.esstu.student.profile.student.porfolio.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ControlTypeResponse(
    val code: String?,
    val name: String?,
    val shortName: String?
)

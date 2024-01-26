package ru.esstu.student.profile.student.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Qualification(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)
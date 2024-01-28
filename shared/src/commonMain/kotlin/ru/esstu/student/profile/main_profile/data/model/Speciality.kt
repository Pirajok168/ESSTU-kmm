package ru.esstu.student.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speciality(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)
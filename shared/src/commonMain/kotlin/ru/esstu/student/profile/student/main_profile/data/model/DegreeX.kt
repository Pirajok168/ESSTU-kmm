package ru.esstu.student.profile.student.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DegreeX(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("shortName")
    val shortName: String?
)
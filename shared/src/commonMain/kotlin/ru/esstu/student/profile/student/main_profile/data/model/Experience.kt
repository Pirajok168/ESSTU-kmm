package ru.esstu.student.profile.student.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    @SerialName("days")
    val days: Int?,
    @SerialName("employeeId")
    val employeeId: Int?,
    @SerialName("experienceType")
    val experienceType: String?,
    @SerialName("months")
    val months: Int?,
    @SerialName("years")
    val years: Int?
)
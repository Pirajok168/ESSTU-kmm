package ru.esstu.student.profile.student.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Degree(
    @SerialName("approveDate")
    val approveDate: String?,
    @SerialName("creationTime")
    val creationTime: Long?,
    @SerialName("defenceDate")
    val defenceDate: String?,
    @SerialName("degree")
    val degree: DegreeX?,
    @SerialName("employeeId")
    val employeeId: Int?,
    @SerialName("fileCode")
    val fileCode: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("modificationTime")
    val modificationTime: Long?,
    @SerialName("number")
    val number: String?,
    @SerialName("organizationName")
    val organizationName: String?,
    @SerialName("organizationPlace")
    val organizationPlace: String?,
    @SerialName("scientificSpeciality")
    val scientificSpeciality: ScientificSpeciality?,
    @SerialName("series")
    val series: String?,
    @SerialName("specialityCode")
    val specialityCode: String?,
    @SerialName("specialityName")
    val specialityName: String?,
    @SerialName("status")
    val status: String?
)
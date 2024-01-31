package ru.esstu.features.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Education(
    @SerialName("creationTime")
    val creationTime: Long?,
    @SerialName("educationType")
    val educationType: EducationType?,
    @SerialName("employeeId")
    val employeeId: Int?,
    @SerialName("fileCode")
    val fileCode: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("institution")
    val institution: Institution?,
    @SerialName("modificationTime")
    val modificationTime: Long?,
    @SerialName("note")
    val note: String?,
    @SerialName("number")
    val number: String?,
    @SerialName("qualification")
    val qualification: Qualification?,
    @SerialName("receiptDate")
    val receiptDate: String?,
    @SerialName("series")
    val series: String?,
    @SerialName("speciality")
    val speciality: Speciality?,
    @SerialName("specialityCode")
    val specialityCode: String?,
    @SerialName("status")
    val status: String?
)
package ru.esstu.student.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rank(
    @SerialName("approveDate")
    val approveDate: String?,
    @SerialName("chair")
    val chair: String?,
    @SerialName("creationTime")
    val creationTime: Long?,
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
    @SerialName("rank")
    val rank: RankX?,
    @SerialName("series")
    val series: String?,
    @SerialName("speciality")
    val speciality: String?,
    @SerialName("status")
    val status: String?
)
package ru.esstu.features.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employment(
    @SerialName("acceptDate")
    val acceptDate: String?,
    @SerialName("contractEndDate")
    val contractEndDate: String?,
    @SerialName("contractFactEndDate")
    val contractFactEndDate: String?,
    @SerialName("contractInfo")
    val contractInfo: String?,
    @SerialName("contractStartDate")
    val contractStartDate: String?,
    @SerialName("department")
    val department: Department?,
    @SerialName("dismissDate")
    val dismissDate: String?,
    @SerialName("employeeCode")
    val employeeCode: String?,
    @SerialName("employeeId")
    val employeeId: Int?,
    @SerialName("post")
    val post: Post?,
    @SerialName("postType")
    val postType: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("wageRate")
    val wageRate: String?,
    @SerialName("workHours")
    val workHours: String?
)
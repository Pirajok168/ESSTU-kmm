package ru.esstu.student.profile.main_profile.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Department(
    @SerialName("departmentCode")
    val departmentCode: String?,
    @SerialName("departmentName")
    val departmentName: String?,
    @SerialName("departmentShortName")
    val departmentShortName: String?,
    @SerialName("departmentType")
    val departmentType: String?,
    @SerialName("departmentTypeName")
    val departmentTypeName: String?,
    @SerialName("higherDepartmentCode")
    val higherDepartmentCode: String?,
    @SerialName("prorectorPostCode")
    val prorectorPostCode: String?
)
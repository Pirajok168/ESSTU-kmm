package ru.esstu.student.profile.student.main_profile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed interface ProfileResponse {

    @Serializable
    data class StudentProfileResponse(
        val chairName: String?,
        val courseNumber: String?,
        val directionCode: String?,
        val directionName: String?,
        val educationLevelCode: String?,
        val educationLevelName: String?,
        val facultyName: String?,
        val firstName: String?,
        val fullName: String?,
        val groupName: String?,
        val lastName: String?,
        val patronymic: String?,
        val photoFileCode: String?,
        val profileName: String?,
        val standardName: String?,
        val status: String?,
        val studentCode: String?,
        val studyFormCode: String?,
        val studyFormName: String?
    ) : ProfileResponse

    @Serializable
    data class EmployeeInfoResponse(
        @SerialName("birthDate")
        val birthDate: Long?,
        @SerialName("degrees")
        val degrees: List<Degree>?,
        @SerialName("educations")
        val educations: List<Education>?,
        @SerialName("employeeId")
        val employeeId: Long?,
        @SerialName("employments")
        val employments: List<Employment>?,
        @SerialName("experience")
        val experience: List<Experience>?,
        @SerialName("firstName")
        val firstName: String?,
        @SerialName("fullName")
        val fullName: String?,
        @SerialName("lastName")
        val lastName: String?,
        @SerialName("patronymic")
        val patronymic: String?,
        @SerialName("photoFileCode")
        val photoFileCode: String?,
        @SerialName("ranks")
        val ranks: List<Rank>?,
        @SerialName("sex")
        val sex: String?
    ) : ProfileResponse
}


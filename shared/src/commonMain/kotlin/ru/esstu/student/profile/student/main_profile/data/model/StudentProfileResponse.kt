package ru.esstu.student.profile.student.main_profile.data.model

import kotlinx.serialization.Serializable


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
)
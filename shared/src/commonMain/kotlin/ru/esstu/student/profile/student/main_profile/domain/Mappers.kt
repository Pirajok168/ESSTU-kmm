package ru.esstu.student.profile.student.main_profile.domain

import ru.esstu.student.profile.student.main_profile.data.model.StudentProfileResponse
import ru.esstu.student.profile.student.main_profile.domain.model.StudentProfile

fun StudentProfileResponse.toStudentProfile() = StudentProfile(
    studyLevel = educationLevelName.orEmpty(),
    standard = standardName.orEmpty(),
    photo = if (photoFileCode != null) "https://esstu.ru/aicstorages/publicDownload/$photoFileCode" else null,
    studyForm = studyFormName.orEmpty(),
    patronymic = patronymic.orEmpty(),
    group = groupName.orEmpty(),
    firstName = firstName.orEmpty(),
    profile = profileName.orEmpty(),
    faculty = facultyName.orEmpty(),
    directionName = directionName.orEmpty(),
    directionCode = directionCode.orEmpty(),
    course = courseNumber.orEmpty(),
    cathedra = chairName.orEmpty(),
    lastName = lastName.orEmpty(),
)

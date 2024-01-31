package ru.esstu.features.profile.main_profile.domain

import ru.esstu.features.profile.main_profile.data.model.ProfileResponse
import ru.esstu.features.profile.main_profile.domain.model.Profile


fun ProfileResponse.StudentProfileResponse.toStudentProfile() = Profile.StudentProfile(
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


fun ProfileResponse.EmployeeInfoResponse.toEmployeeProfile() = Profile.EmployeeProfile(
    lastName = lastName.orEmpty(),
    firstName = firstName.orEmpty(),
    patronymic = patronymic.orEmpty(),
    ranks = ranks?.map {
        "${it.rank?.name.orEmpty()}, ${it.chair}".trim().trim(',')
    }.orEmpty(),
    education = educations?.map {
        Profile.EmployeeProfile.Education(
            institutionName = it.institution?.name,
            speciality = it.speciality?.name,
            qualification = it.qualification?.name
        )
    }.orEmpty(),
    degrees = degrees?.map {
        Profile.EmployeeProfile.Degrees(
            degreeName = it.degree?.name.orEmpty(),
            scientificSpeciality = it.scientificSpeciality?.name.orEmpty(),
            code = it.scientificSpeciality?.code.orEmpty(),
        )
    }.orEmpty()
)
package ru.esstu.student.profile.main_profile.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.main_profile.data.model.ProfileResponse

interface ProfileApi {

    suspend fun getStudentProfileInfo(): Response<ProfileResponse.StudentProfileResponse>

    suspend fun getEmployeeProfileInfo(): Response<ProfileResponse.EmployeeInfoResponse>


    suspend fun updatePhoto(
        authToken: String,
        files: List<CachedFile>,
    )
}
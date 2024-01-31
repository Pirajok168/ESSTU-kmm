package ru.esstu.features.profile.main_profile.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.CachedFile

interface ProfileApi {

    suspend fun getStudentProfileInfo(): Response<ru.esstu.features.profile.main_profile.data.model.ProfileResponse.StudentProfileResponse>

    suspend fun getEmployeeProfileInfo(): Response<ru.esstu.features.profile.main_profile.data.model.ProfileResponse.EmployeeInfoResponse>


    suspend fun updatePhoto(
        authToken: String,
        files: List<CachedFile>,
    )
}
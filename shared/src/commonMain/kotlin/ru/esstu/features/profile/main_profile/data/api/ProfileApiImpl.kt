package ru.esstu.features.profile.main_profile.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.CachedFile

class ProfileApiImpl(
    private val authorizedApi: AuthorizedApi
) : ProfileApi {
    override suspend fun getStudentProfileInfo(): Response<ru.esstu.features.profile.main_profile.data.model.ProfileResponse.StudentProfileResponse> =
        authorizedApi.get(path = "/lk/api/v1/student/getInfo")

    override suspend fun getEmployeeProfileInfo(): Response<ru.esstu.features.profile.main_profile.data.model.ProfileResponse.EmployeeInfoResponse> =
        authorizedApi.get(path = "/lk/api/v1/employee/getInfo")


    override suspend fun updatePhoto(authToken: String, files: List<CachedFile>) {
        TODO("Not yet implemented")
    }
}
package ru.esstu.student.profile.student.main_profile.data.api

import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.main_profile.data.model.ProfileResponse

class ProfileApiImpl(
    private val authorizedApi: AuthorizedApi
): ProfileApi {
    override suspend fun getStudentProfileInfo(): Response<ProfileResponse.StudentProfileResponse> =
         authorizedApi.get(path = "/lk/api/v1/student/getInfo")

    override suspend fun getEmployeeProfileInfo(): Response<ProfileResponse.EmployeeInfoResponse> =
        authorizedApi.get(path = "/lk/api/v1/employee/getInfo")


    override suspend fun updatePhoto(authToken: String, files: List<CachedFile>) {
        TODO("Not yet implemented")
    }
}
package ru.esstu.student.profile.student.main_profile.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.path
import okio.FileSystem
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.main_profile.data.model.StudentProfileResponse

class ProfileApiImpl(
    private val portalApi: HttpClient
): ProfileApi {
    override suspend fun getStudentInfo(authToken: String): StudentProfileResponse {
        val response = portalApi.get{
            url{
                path("/lk/api/v1/student/getInfo")
                bearerAuth(authToken)
            }
        }
        return response.body()
    }

    override suspend fun updatePhoto(authToken: String, files: List<CachedFile>) {
        TODO("Not yet implemented")
    }
}
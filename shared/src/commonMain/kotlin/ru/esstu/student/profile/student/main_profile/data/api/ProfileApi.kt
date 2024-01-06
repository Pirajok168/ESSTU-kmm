package ru.esstu.student.profile.student.main_profile.data.api

import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.main_profile.data.model.StudentProfileResponse

interface ProfileApi {

    suspend fun getStudentInfo(
         authToken: String,
    ): StudentProfileResponse


    suspend fun updatePhoto(
        authToken: String,
        files: List<CachedFile>,
    )
}
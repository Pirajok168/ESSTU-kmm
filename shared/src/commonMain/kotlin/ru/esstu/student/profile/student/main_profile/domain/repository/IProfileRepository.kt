package ru.esstu.student.profile.student.main_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.main_profile.domain.model.StudentProfile

interface IProfileRepository {
    suspend fun getProfile(): Flow<FlowResponse<StudentProfile>>

    suspend fun updatePhoto(photo: CachedFile): Response<String>
}
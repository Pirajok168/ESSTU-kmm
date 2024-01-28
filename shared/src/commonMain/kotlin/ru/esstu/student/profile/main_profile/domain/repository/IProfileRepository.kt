package ru.esstu.student.profile.main_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.main_profile.domain.model.Profile

interface IProfileRepository {
    suspend fun getProfile(): Flow<FlowResponse<out Profile>>

    suspend fun updatePhoto(photo: CachedFile): Response<String>
}
package ru.esstu.features.profile.main_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.profile.main_profile.domain.model.Profile
import ru.esstu.student.messaging.entities.CachedFile


interface IProfileRepository {
    suspend fun getProfile(): Flow<FlowResponse<out Profile>>

    suspend fun updatePhoto(photo: CachedFile): Response<String>
}
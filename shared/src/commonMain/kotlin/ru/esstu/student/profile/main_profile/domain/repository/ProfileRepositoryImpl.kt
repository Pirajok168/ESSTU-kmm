package ru.esstu.student.profile.main_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.main_profile.domain.model.Profile
import ru.esstu.student.profile.main_profile.domain.toEmployeeProfile
import ru.esstu.student.profile.main_profile.domain.toStudentProfile
import ru.esstu.student.profile.main_profile.data.api.ProfileApi

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val auth: IAuthRepository,
): IProfileRepository {
    override suspend fun getProfile(): Flow<FlowResponse<out Profile>> = flow {
        auth.provideToken { token ->

            val owner = token.owner
            emit(FlowResponse.Loading())

            val remoteProfile = when(owner){
                is TokenOwners.Student -> api.getStudentProfileInfo()
                    .transform { it.toStudentProfile() }
                is TokenOwners.Teacher -> api.getEmployeeProfileInfo()
                    .transform { it.toEmployeeProfile() }
                else -> null
            }

            when (remoteProfile) {
                is Response.Error -> emit(FlowResponse.Error(remoteProfile.error))
                is Response.Success -> {
                    emit(FlowResponse.Success(remoteProfile.data))
                }
                else -> {}
            }
            emit(FlowResponse.Loading(false))
        }
    }

    override suspend fun updatePhoto(photo: CachedFile): Response<String> {
        TODO("Not yet implemented")
    }
}
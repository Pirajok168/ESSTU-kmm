package ru.esstu.student.profile.student.main_profile.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.main_profile.data.api.ProfileApi
import ru.esstu.student.profile.student.main_profile.domain.model.StudentProfile
import ru.esstu.student.profile.student.main_profile.domain.toStudentProfile

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val auth: IAuthRepository,
): IProfileRepository {
    override suspend fun getProfile(): Flow<FlowResponse<StudentProfile>> = flow {
        auth.provideToken { token ->

            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken
            emit(FlowResponse.Loading())

            val remoteProfile = api.getStudentInfo().transform {
                it.toStudentProfile()
            }

            when (remoteProfile) {
                is Response.Error -> emit(FlowResponse.Error(remoteProfile.error))
                is Response.Success -> {
                    emit(FlowResponse.Success(remoteProfile.data))
                }
            }
            emit(FlowResponse.Loading(false))
        }
    }

    override suspend fun updatePhoto(photo: CachedFile): Response<String> {
        TODO("Not yet implemented")
    }
}
package ru.esstu.student.profile.student.porfolio.achivement.domain.repository

import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.achivement.data.api.AchievementApi
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Achievement
import ru.esstu.student.profile.student.porfolio.achivement.domain.toAchievement

class AchievementsRepositoryImpl(
    private val api: AchievementApi,
    private val auth: IAuthRepository
): IAchievementsRepository {
    override fun getAchievements(): Flow<FlowResponse<List<Achievement>>> = flow {
        auth.provideToken { token ->

            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw IOException("unsupported user category")
            emit(FlowResponse.Loading())

            //val cachedFiles = achievementDao.getAchievements(appUserId).map { it.toAchievement(cachedFilesDao.getCachedAttachment(it.id)) }

/*
            if (cachedFiles.isNotEmpty())
                emit(FlowResponse.Success(cachedFiles))
*/

            val remoteFiles = auth.provideToken { type, access ->
                api.getAchievements("$type $access").mapNotNull { it.toAchievement() }
            }

            when (remoteFiles) {
                is Response.Error ->
                    emit(FlowResponse.Error(remoteFiles.error))
                is Response.Success -> {
                  //  achievementDao.updateFiles(remoteFiles.data.map { it.toEntity(appUserId) })
                    emit(FlowResponse.Success(remoteFiles.data))
                }
            }

            emit(FlowResponse.Loading(false))
        }
    }
}
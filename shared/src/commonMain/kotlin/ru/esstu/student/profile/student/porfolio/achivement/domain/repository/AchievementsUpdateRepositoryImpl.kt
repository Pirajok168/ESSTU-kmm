package ru.esstu.student.profile.student.porfolio.achivement.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.UpdateEvents
import ru.esstu.student.profile.student.porfolio.achivement.data.api.AchievementApi
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Achievement
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Attachment

class AchievementsUpdateRepositoryImpl(
    private val api: AchievementApi,
    private val auth: IAuthRepository
): IAchievementsUpdateRepository {
    override val updateFlow: Flow<UpdateEvents<Achievement>>
        get() = TODO("Not yet implemented")

    override suspend fun createAchievement(
        title: String,
        date: Long,
        status: String,
        attachment: Attachment?
    ): Response<Achievement> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAchievement(
        achievement: Achievement,
        attachment: Attachment?
    ): Response<Achievement> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAchievement(achievement: Achievement): Response<Unit> {
        TODO("Not yet implemented")
    }
}
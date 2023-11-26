package ru.esstu.student.profile.student.porfolio.achivement.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.UpdateEvents
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Achievement
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Attachment

interface IAchievementsUpdateRepository {
    val updateFlow: Flow<UpdateEvents<Achievement>>

    suspend fun createAchievement(title: String, date: Long, status: String, attachment: Attachment?): Response<Achievement>

    suspend fun updateAchievement(achievement: Achievement, attachment: Attachment?): Response<Achievement>

    suspend fun deleteAchievement(achievement: Achievement): Response<Unit>
}
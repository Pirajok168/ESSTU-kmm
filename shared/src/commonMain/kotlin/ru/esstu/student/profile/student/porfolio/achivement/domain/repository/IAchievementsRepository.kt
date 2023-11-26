package ru.esstu.student.profile.student.porfolio.achivement.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.student.profile.student.porfolio.achivement.domain.model.Achievement

interface IAchievementsRepository {
    fun getAchievements(): Flow<FlowResponse<List<Achievement>>>
}
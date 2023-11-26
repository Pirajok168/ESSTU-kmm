package ru.esstu.student.profile.student.porfolio.achivement.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.achivement.data.model.AchievementRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

interface AchievementApi {


    suspend fun getAchievements(

        authToken: String
    ): List<PortfolioFileRequestResponse>


    suspend fun deleteAchievement(

        authToken: String,

        id: Int
    ): Response<Unit>


    suspend fun updateAchievement(

        authToken: String,
        attachment: String? = null,

        achievement: PortfolioFileRequestResponse
    ): PortfolioFileRequestResponse


    suspend fun createAchievement(

        authToken: String,

        attachment: String? = null,

        Achievement: AchievementRequest
    ): PortfolioFileRequestResponse
}
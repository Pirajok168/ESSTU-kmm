package ru.esstu.student.profile.student.porfolio.awards.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.awards.data.model.AwardRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

interface AwardsApi {
    companion object {
        const val BASE_URL = "https://esstu.ru"
    }


    suspend fun getAwards(
        authToken: String
    ): List<PortfolioFileRequestResponse>


    suspend fun deleteAward(
        id: Int,
        authToken: String
    ): Response<Unit>


    suspend fun updateAward(
        attachment: String? = null,
        award: PortfolioFileRequestResponse,
        authToken: String
    ): PortfolioFileRequestResponse


    suspend fun createAward(
        attachment: String? = null,
        award: AwardRequest,
        authToken: String
    ): PortfolioFileRequestResponse
}
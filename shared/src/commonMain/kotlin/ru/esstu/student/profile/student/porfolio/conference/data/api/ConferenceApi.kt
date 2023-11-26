package ru.esstu.student.profile.student.porfolio.conference.data.api

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.conference.data.model.ConferenceRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

interface ConferenceApi {


    suspend fun getConferences(
        authToken: String
    ): List<PortfolioFileRequestResponse>


    suspend fun deleteConference(
        id: Int,
        authToken: String
    ): Response<Unit>


    suspend fun updateConference(
        attachment: String? = null,
        conference: PortfolioFileRequestResponse,
        authToken: String
    ): PortfolioFileRequestResponse


    suspend fun createConference(
        attachment: String? = null,
        conference: ConferenceRequest,
        authToken: String
    ): PortfolioFileRequestResponse
}
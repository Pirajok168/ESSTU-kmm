package ru.esstu.student.profile.student.porfolio.conference.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.path
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.awards.data.model.AwardRequest
import ru.esstu.student.profile.student.porfolio.conference.data.model.ConferenceRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

class ConferenceApiImpl(
    private val portalApi: HttpClient,
): ConferenceApi {
    override suspend fun getConferences(authToken: String): List<PortfolioFileRequestResponse> {
        val response = portalApi.get{
            url{
                path("/lk/api/v1/student/portfolio/getElements?type=Conference")
                bearerAuth(authToken)
            }
        }
        return response.body()
    }

    override suspend fun deleteConference(id: Int, authToken: String): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateConference(
        attachment: String?,
        conference: PortfolioFileRequestResponse,
        authToken: String
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createConference(
        attachment: String?,
        conference: ConferenceRequest,
        authToken: String
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }

}
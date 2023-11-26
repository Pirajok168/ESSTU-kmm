package ru.esstu.student.profile.student.porfolio.awards.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.path
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.awards.data.model.AwardRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

class AwardsApiImpl(
    private val portalApi: HttpClient,
): AwardsApi {
    override suspend fun getAwards(authToken: String): List<PortfolioFileRequestResponse> {
        val response = portalApi.get{
            url{
                path("/lk/api/v1/student/portfolio/getElements?type=AWARD")
                bearerAuth(authToken)
            }
        }
        return response.body()
    }

    override suspend fun deleteAward(id: Int, authToken: String): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAward(
        attachment: String?,
        award: PortfolioFileRequestResponse,
        authToken: String
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createAward(
        attachment: String?,
        award: AwardRequest,
        authToken: String
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }
}
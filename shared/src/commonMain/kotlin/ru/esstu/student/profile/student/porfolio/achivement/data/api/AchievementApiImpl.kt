package ru.esstu.student.profile.student.porfolio.achivement.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.http.path
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.profile.student.porfolio.achivement.data.model.AchievementRequest
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse

class AchievementApiImpl(
    private val portalApi: HttpClient,
): AchievementApi {
    override suspend fun getAchievements(authToken: String): List<PortfolioFileRequestResponse> {
        val response = portalApi.get{
            url{
                path("/lk/api/v1/student/portfolio/getElements?type=ACHIEVEMENT")
                bearerAuth(authToken)
            }
        }
        return response.body()
    }

    override suspend fun deleteAchievement(authToken: String, id: Int): Response<Unit> {
        val response = portalApi.delete {
            url{
                path("/lk/api/v1/student/portfolio/removeElement")
                bearerAuth(authToken)
                encodedParameters.append("id", id.toString())
            }
        }
        return response.body()
    }

    override suspend fun updateAchievement(
        authToken: String,
        attachment: String?,
        achievement: PortfolioFileRequestResponse
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }

    override suspend fun createAchievement(
        authToken: String,
        attachment: String?,
        Achievement: AchievementRequest
    ): PortfolioFileRequestResponse {
        TODO("Not yet implemented")
    }
}
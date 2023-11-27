package ru.esstu.student.profile.student.porfolio.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.path
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioFileRequestResponse
import ru.esstu.student.profile.student.porfolio.data.model.PortfolioTypeResponse

class PortfolioApiImpl(
    private val portalApi: HttpClient,
): PortfolioApi {

    override suspend fun getFilesPortfolioByType(
        type: PortfolioTypeResponse,
        authToken: String
    ): List<PortfolioFileRequestResponse> {

        val response = portalApi.get {
            url {
                path("/lk/api/v1/student/portfolio/getElements")
                encodedParameters.append("type", "$type")
                bearerAuth(authToken)
            }
        }

        return response.body()
    }
}
package ru.news.announcement.data.web

import io.ktor.client.request.bearerAuth
import io.ktor.http.path
import ru.esstu.core.ktor.AuthorizedHttpClient
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response

class NewsApiImpl(
    private val portalApi: AuthorizedHttpClient,
): NewsApi {
    override suspend fun getAnnouncements(authToken: String, offset: Int, limit: Int): Response<DataResponse> {
        val response = portalApi.get<DataResponse> {
            url {
                path("lk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "ANNOUNCEMENT")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }

        return response
    }
}
package ru.esstu.student.messaging.messenger.supports.datasource.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class SupportsApiImpl(
    private val portalApi: HttpClient
): SupportsApi {
    override suspend fun getSupports(authToken: String, offset: Int, limit: Int): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "SUPPORT")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
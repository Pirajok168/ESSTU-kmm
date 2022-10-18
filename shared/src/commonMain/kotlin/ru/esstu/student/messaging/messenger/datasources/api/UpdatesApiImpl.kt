package ru.esstu.student.messaging.messenger.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class UpdatesApiImpl(
    private val portalApi: HttpClient
): UpdatesApi {
    override suspend fun getUpdates(authToken: String, timestamp: Long): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/async/messenger/getGlobalUpdates")
                bearerAuth(authToken)
                encodedParameters.append("timestamp", timestamp.toString())
            }
        }
        return response.body()
    }
}
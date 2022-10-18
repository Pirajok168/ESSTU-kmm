package ru.esstu.student.messaging.messenger.dialogs.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class DialogsApiImpl(
    private val portalApi: HttpClient
): DialogsApi {
    override suspend fun getDialogs(authToken: String, offset: Int, limit: Int): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "DIALOGUE")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
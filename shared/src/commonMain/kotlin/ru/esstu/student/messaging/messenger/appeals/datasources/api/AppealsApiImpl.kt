package ru.esstu.student.messaging.messenger.appeals.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

class AppealsApiImpl(
    private val portalApi: HttpClient
): AppealsApi {
    override suspend fun getAppeals(authToken: String, offset: Int, limit: Int): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/v2/messenger/getDialogs?type=APPEAL")
                bearerAuth(authToken)
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
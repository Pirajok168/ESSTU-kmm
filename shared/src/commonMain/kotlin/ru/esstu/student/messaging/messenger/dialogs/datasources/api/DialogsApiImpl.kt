package ru.esstu.student.messaging.messenger.dialogs.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse
import kotlin.collections.get

class DialogsApiImpl(
    private val portalApi: HttpClient
): DialogsApi {
    override suspend fun getDialogs(authToken: String, offset: Int, limit: Int): DataResponse {
        val response = portalApi.get {
            url {
                path("lk/api/v2/messenger/getDialogs?type=DIALOGUE")
                bearerAuth(authToken)
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }
        return response.body()
    }
}
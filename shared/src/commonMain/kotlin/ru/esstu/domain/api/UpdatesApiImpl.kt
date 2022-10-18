package ru.esstu.domain.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import ru.esstu.domain.response.entities.DataResponse

class UpdatesApiImpl(
    private val portalApi: HttpClient,
): UpdatesApi {
    override suspend fun getUpdates(authToken: String, timestamp: Long): DataResponse {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }.decodeFromJsonElement(portalApi.get {
            url {
                path("lk/api/async/messenger/getGlobalUpdates")
                bearerAuth(authToken)
                parameters.append("time", timestamp.toString())
            }
        }.body())

    }
}
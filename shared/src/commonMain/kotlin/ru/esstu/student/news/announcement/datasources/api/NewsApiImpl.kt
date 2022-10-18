package ru.esstu.student.news.announcement.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class NewsApiImpl(
    private val portalApi: HttpClient,
): NewsApi {
    override suspend fun getAnnouncements(authToken: String, offset: Int, limit: Int): DataResponse {


        val response = portalApi.get() {
            url {
                path("lk/api/v2/messenger/getDialogs")
                bearerAuth(authToken)
                encodedParameters.append("type", "ANNOUNCEMENT")
                encodedParameters.append("offset", offset.toString())
                encodedParameters.append("limit", limit.toString())
            }
        }


        return response.body()
    }
}
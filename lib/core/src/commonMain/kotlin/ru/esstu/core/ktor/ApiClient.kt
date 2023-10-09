package ru.esstu.core.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpStatusCode
import ru.esstu.domain.utill.wrappers.Response

class ApiClient(
    private val client: () -> HttpClient
) {
    internal suspend inline fun <reified T> request(
        method: HttpClient.() -> T
    ): Response<T> {
        var response: HttpResponse? = null
        var status: HttpStatusCode? = null

        return with(runCatching {
            val client = client()
            client.responsePipeline.intercept(HttpResponsePipeline.Parse) {
                response = context.response
                status = context.response.status
            }

            client.method()
        }) {
            status?.also {
                if (it == H)
            }
        }
    }

}
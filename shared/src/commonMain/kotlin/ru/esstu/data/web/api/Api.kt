package ru.esstu.data.web.api

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.api.model.ServerErrors


abstract class Api(
    private val client: () -> HttpClient,
    internal val json: Json
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
                if (it == HttpStatusCode.Unauthorized) {

                    return Response.Error(ResponseError(error = ServerErrors.Unauthorized))
                } else if (it.value in 500..505) {
                    return Response.Error(ResponseError(error = ServerErrors.Unresponsive))
                } else if (it == HttpStatusCode.BadRequest && response != null) {
                    // Обработка ошибок, которые могут быть, пока я не знаю какие будут поэтому дефолт Unknown
                    return Response.Error(
                        ResponseError(
                            error = processKnownServerError(response!!.bodyAsText())
                                ?: ServerErrors.Unknown
                        )
                    )
                }
            }

            runCatching {
                (getOrThrow() as? HttpResponse)?.let { response ->
                    if (response.status == HttpStatusCode.NoContent) {
                        return Response.Success(getOrThrow())
                    }
                }
            }.onFailure {
                exceptionOrNull()
                    ?.also { e ->
                        Napier.e(message = e.message.orEmpty(), throwable = e.cause)
                        if (e !is kotlin.coroutines.cancellation.CancellationException) {
                            return Response.Error(
                                ResponseError(error = ServerErrors.Unknown)
                            )
                        }
                    }
            }

            Response.Success(getOrThrow())
        }
    }

    private fun processKnownServerError(errorBody: String): ServerErrors? =
        kotlin.runCatching {
            json.decodeFromString<SimpleError>(errorBody).let {
                if (it.error_description == "Неверное имя пользователя или пароль" || it.error_description?.contains(
                        "Invalid refresh token"
                    ) == true
                ) {
                    ServerErrors.Unauthorized
                } else {
                    ServerErrors.Unknown
                }
            }
        }.getOrNull()

}

@Serializable
data class SimpleError(
    val error: String?,
    val error_description: String?
)
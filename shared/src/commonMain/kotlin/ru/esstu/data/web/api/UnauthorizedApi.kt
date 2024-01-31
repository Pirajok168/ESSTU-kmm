@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package ru.esstu.data.web.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent
import kotlinx.serialization.json.Json
import ru.esstu.data.web.api.model.Response

class UnauthorizedApi(
    client: () -> HttpClient,
    json: Json
) : Api(client, json) {

    suspend inline fun <reified T> get(
        path: String,
        body: Any = EmptyContent,
        host: String = "esstu.ru",
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response<T> = request {
        get {
            url(host = host, path = path) {
                setBody(body)
            }
            block()
        }.body()
    }


    suspend inline fun <reified T> post(
        path: String,
        body: Any = EmptyContent,
        host: String = "esstu.ru",
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response<T> = request {
        post {
            url(host = host, path = path) {
                setBody(body)
            }
            block()
        }.body()
    }

}
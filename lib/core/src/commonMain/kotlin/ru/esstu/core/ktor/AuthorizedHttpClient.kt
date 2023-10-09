@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "LongParameterList")

package ru.esstu.core.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import ru.esstu.domain.utill.wrappers.Response

class AuthorizedHttpClient internal constructor(
     client: () -> HttpClient
): ApiClient(client) {
    suspend inline fun <reified T> get(
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response<T> = checkedRequest {
            get {
                block()
            }.body()
        }

    internal suspend inline fun <reified T> checkedRequest(method: HttpClient.() -> T) : Response<T>{
        return request(method)
    }
}
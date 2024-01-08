@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package ru.esstu.domain.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent
import kotlinx.serialization.json.Json
import ru.esstu.auth.datasources.local.ITokenDSManager
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors

class AuthorizedApi(
    private val loginDataRepository: ITokenDSManager,
    client: () -> HttpClient,
    json: Json
): Api(client, json) {

    suspend inline fun <reified T> get(
        path: String,
        body: Any = EmptyContent,
        host: String = "esstu.ru",
        block: HttpRequestBuilder.() -> Unit = {}
    ) : Response<T> = checkedRequest {
        get {
            url(host = host, path = path) {
                setBody(body)
                bearerAuth(loginDataRepository.getAccessToken()?.accessToken ?: return@url)
            }
            block()
        }.body()
    }

    suspend inline fun <reified T> post(
        path: String,
        body: Any = EmptyContent,
        host: String = "esstu.ru",
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response<T> = checkedRequest {
        post {
            url(host = host, path = path,) {
                setBody(body)
                bearerAuth(loginDataRepository.getAccessToken()?.accessToken ?: return@url)
            }
            block()
        }.body()
    }

    internal suspend inline fun <reified T> checkedRequest(method: HttpClient.() -> T): Response<T> {
        loginDataRepository.getToken()?.accessToken ?: return Response.Error(ResponseError(error = ServerErrors.Unknown))



        return request(method)
    }

}
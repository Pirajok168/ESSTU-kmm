@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package ru.esstu.data.web.api

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import ru.esstu.auth.data.api.student_teacher.AuthApi
import ru.esstu.data.token.repository.ILoginDataRepository
import ru.esstu.data.token.toToken
import ru.esstu.data.token.toTokenPair
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.api.model.ServerErrors
import ru.esstu.data.web.api.model.doOnSuccess

class AuthorizedApi(
    private val loginDataRepository: ILoginDataRepository,
    private val portalApi: AuthApi,
    client: () -> HttpClient,
    json: Json
) : Api(client, json) {

    suspend inline fun <reified T> get(
        path: String,
        host: String = "esstu.ru",
        block: HttpRequestBuilder.() -> Unit = {}
    ): Response<T> = checkedRequest {
        get {
            url(host = host, path = path) {
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
            url(host = host, path = path) {
                setBody(body)
                contentType(ContentType.Application.Json)
                bearerAuth(loginDataRepository.getAccessToken()?.accessToken ?: return@url)
            }
            block()
        }.body()
    }

    internal suspend inline fun <reified T> checkedRequest(method: HttpClient.() -> T): Response<T> {
        if (!loginDataRepository.isUserAuthorized()) {
            return tryRefreshAuthToken(method)
        }


        return request(method)
    }

    internal suspend inline fun <reified T> tryRefreshAuthToken(method: HttpClient.() -> T): Response<T> {
        Napier.d("tryRefreshAuthToken", tag = "tryRefreshAuthToken")
        val token = loginDataRepository.getToken()?.toToken()
            ?: return Response.Error(ResponseError(error = ServerErrors.Unauthorized))
        val response = portalApi.refreshToken(token.refresh).transform { it.toToken() }

        response
            .doOnSuccess {
                it?.let {
                    loginDataRepository.setToken(it.toTokenPair())
                    it.expiresIn?.let {
                        loginDataRepository.setExpiresDateToken(
                            Clock.System.now().toEpochMilliseconds().plus(it)
                        )
                    }
                }
            }
        return when (response) {
            is Response.Error -> {
                Response.Error(ResponseError(error = ServerErrors.Unauthorized))
            }

            is Response.Success -> {
                request(method)
            }
        }
    }

}
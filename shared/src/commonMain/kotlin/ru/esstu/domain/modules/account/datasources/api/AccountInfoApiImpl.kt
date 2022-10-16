package ru.esstu.domain.modules.account.datasources.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.esstu.domain.modules.account.datasources.api.response.UserResponse

class AccountInfoApiImpl(
    private val portalApi: HttpClient
): AccountInfoApi {
    override suspend fun getUser(authToken: String, userId: String): UserResponse {
        val response = portalApi.get {
            url {
                path("mlk/api/v2/messenger/getUserFull")
                bearerAuth(authToken)
                encodedParameters.append("userId", userId)
            }
        }
        return response.body()
    }
}
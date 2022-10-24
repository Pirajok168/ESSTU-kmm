package ru.esstu.auth.datasources.api.student_teacher

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement


class AuthApImpl(
    private val portalApi: HttpClient,
): AuthApi {
    override suspend fun refreshToken(refresh: String): Tokens {
        val request = portalApi.post {
            url{
                path("/auth/oauth/token")
                setBody(FormDataContent(Parameters.build {
                    append("response_type", "token")
                    append("grant_type", "refresh_token")
                    append("scope", "trust")
                    append("client_id", "personal_office_mobile")
                    append("client_secret", "431ecc50c5be014224ec1abf8c2f99840ca4c43e15a7db56bcf8d0b1b6ef632e")
                    append("refresh_token", refresh)
                }))
            }

        }
        return request.body()

    }

    override suspend fun auth(login: String, Password: String): Tokens {
        val httpRequest = portalApi.post {
            url {
                path("/auth/oauth/token")
                setBody(FormDataContent(Parameters.build {
                    append("username", login)
                    append("password", Password)
                    append("response_type", "token")
                    append("grant_type", "password")
                    append("scope", "trust")
                    append("client_id", "personal_office_mobile")
                    append("client_secret", "431ecc50c5be014224ec1abf8c2f99840ca4c43e15a7db56bcf8d0b1b6ef632e")
                }))

            }
        }

        return httpRequest.body()

    }
}
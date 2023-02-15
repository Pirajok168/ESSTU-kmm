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
                    append("client_id", "personal_office_employee")
                    append("client_secret", "ецй")
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
                    append("client_id", "personal_office_employee")
                    append("client_secret", "йцу")
                }))

            }
        }

        return httpRequest.body()

    }
}
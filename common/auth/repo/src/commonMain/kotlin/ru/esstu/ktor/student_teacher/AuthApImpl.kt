package ru.esstu.ktor.student_teacher

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.esstu.student_teacher.entities.Tokens
import ru.esstu.student_teacher.AuthApi


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
                    append("client_secret", "0a36339cfb8136da2151301170a730d758a88c0f130bd15fc1abe583a91ccfae")
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
                    append("client_secret", "0a36339cfb8136da2151301170a730d758a88c0f130bd15fc1abe583a91ccfae")
                }))

            }
        }

        return httpRequest.body()

    }
}
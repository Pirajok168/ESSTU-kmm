package ru.esstu.auth.data.api.student_teacher

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import ru.esstu.auth.data.api.model.Tokens
import ru.esstu.data.web.api.UnauthorizedApi
import ru.esstu.data.web.api.model.Response


class AuthApImpl(
    private val unauthorizedApi: UnauthorizedApi
) : AuthApi {
    override suspend fun refreshToken(refresh: String): Response<Tokens> {

        return unauthorizedApi.post(
            path = "/auth/oauth/token",
        ) {
            setBody(FormDataContent(Parameters.build {
                append("response_type", "token")
                append("grant_type", "refresh_token")
                append("scope", "trust")
                append("client_id", "personal_office_employee")
                append(
                    "client_secret",
                    "0a36339cfb8136da2151301170a730d758a88c0f130bd15fc1abe583a91ccfae"
                )
                append("refresh_token", refresh)
            }))
        }

    }

    override suspend fun auth(login: String, Password: String): Response<Tokens> {

        return unauthorizedApi.post(
            path = "/auth/oauth/token",
        ) {
            setBody(FormDataContent(Parameters.build {
                append("username", login)
                append("password", Password)
                append("response_type", "token")
                append("grant_type", "password")
                append("scope", "trust")
                append("client_id", "personal_office_employee")
                append(
                    "client_secret",
                    "0a36339cfb8136da2151301170a730d758a88c0f130bd15fc1abe583a91ccfae"
                )
            }))
        }

    }
}
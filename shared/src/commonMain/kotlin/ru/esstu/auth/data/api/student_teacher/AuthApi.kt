package ru.esstu.auth.data.api.student_teacher

import ru.esstu.auth.data.api.model.Tokens
import ru.esstu.data.web.api.model.Response


interface AuthApi {
    suspend fun refreshToken(refresh: String): Response<Tokens>
    suspend fun auth(login: String, Password: String): Response<Tokens>
}
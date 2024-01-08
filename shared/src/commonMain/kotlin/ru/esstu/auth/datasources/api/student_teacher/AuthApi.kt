package ru.esstu.auth.datasources.api.student_teacher

import ru.esstu.domain.utill.wrappers.Response


interface AuthApi {
    suspend fun refreshToken(refresh: String): Response<Tokens>
    suspend fun auth(login: String, Password: String): Response<Tokens>
}
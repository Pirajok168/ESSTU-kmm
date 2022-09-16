package ru.esstu.auth.datasources.api.student_teacher


interface AuthApi {
    suspend fun refreshToken(refresh: String): Tokens
    suspend fun auth(login: String, Password: String): Tokens
}
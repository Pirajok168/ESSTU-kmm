package ru.esstu.student_teacher

import ru.esstu.student_teacher.entities.Tokens


interface AuthApi {
    suspend fun refreshToken(refresh: String): Tokens
    suspend fun auth(login: String, Password: String): Tokens
}
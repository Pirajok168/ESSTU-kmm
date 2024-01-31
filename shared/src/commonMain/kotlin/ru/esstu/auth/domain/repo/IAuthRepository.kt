package ru.esstu.auth.domain.repo

import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.auth.domain.model.Token
import ru.esstu.data.web.api.model.Response


interface IAuthRepository {
    val logoutFlow: SharedFlow<Token?>
    suspend fun refreshToken(): Response<Token>
    suspend fun auth(login: String, Password: String): Response<Token>
    suspend fun entrantAuth(login: String, Password: String): Response<Token>
    suspend fun <T> provideToken(call: suspend (type: String, token: String) -> T): Response<T>
    suspend fun <T> provideToken(call: suspend (token: Token) -> T): Response<T>
    suspend fun guestAuth(): Response<Token>
}
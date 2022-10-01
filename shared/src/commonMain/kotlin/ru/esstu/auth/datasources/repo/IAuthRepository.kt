package ru.esstu.auth.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.auth.entities.Token
import ru.esstu.domain.utill.wrappers.Response


interface IAuthRepository {
    val logoutFlow: Flow<Token?>
    suspend fun refreshToken(): Response<Token>
    suspend fun auth(login: String, Password: String): Response<Token>
    suspend fun entrantAuth(login: String, Password: String): Response<Token>
    suspend fun <T> provideToken(call: suspend (type:String, token: String) -> T): Response<T>
    suspend fun <T> provideToken(call: suspend (token: Token) -> T): Response<T>
    suspend fun guestAuth(): Response<Token>
}
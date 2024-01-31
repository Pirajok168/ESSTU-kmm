package ru.esstu.data.token.repository

import ru.esstu.data.token.model.TokenPair

interface ILoginDataRepository {

    fun getAccessToken(): TokenPair?
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)

    suspend fun setExpiresDateToken(expiresDate: Long)

    suspend fun isUserAuthorized(): Boolean
}
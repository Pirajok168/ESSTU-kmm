package ru.esstu.auth.datasources.local

interface ILoginDataRepository {

    fun getAccessToken(): TokenPair?
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)

    suspend fun setExpiresDateToken(expiresDate: Long)

    suspend fun isUserAuthorized(): Boolean
}
package ru.esstu.auth.datasources.local

interface ITokenDSManager {

    fun getAccessToken(): TokenPair?
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)
}
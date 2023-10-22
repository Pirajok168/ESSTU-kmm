package ru.esstu.core.local

interface ITokenDSManager {
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)
}
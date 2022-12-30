package ru.esstu

import ru.esstu.entities.TokenPair

interface ITokenDSManager {
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)
}
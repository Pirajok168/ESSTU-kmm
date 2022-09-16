package ru.esstu.auth.datasources.local

import kotlinx.coroutines.flow.Flow

interface ITokenDSManager {
    suspend fun getToken(): TokenPair?
    suspend fun setToken(tokens: TokenPair?)
}
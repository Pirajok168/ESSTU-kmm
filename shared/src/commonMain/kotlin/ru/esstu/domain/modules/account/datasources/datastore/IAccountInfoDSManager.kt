package ru.esstu.domain.modules.account.datasources.datastore

import kotlinx.coroutines.flow.Flow

interface IAccountInfoDSManager {
    val getUser: Flow<UserEntity?>
    suspend fun setUser(user: UserEntity)

    suspend fun clearUser()
}
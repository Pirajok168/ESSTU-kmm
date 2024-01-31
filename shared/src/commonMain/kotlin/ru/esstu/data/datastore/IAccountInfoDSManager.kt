package ru.esstu.data.datastore

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.datastore.model.UserEntity

interface IAccountInfoDSManager {
    val getUser: Flow<UserEntity?>
    suspend fun setUser(user: UserEntity)

    suspend fun clearUser()
}
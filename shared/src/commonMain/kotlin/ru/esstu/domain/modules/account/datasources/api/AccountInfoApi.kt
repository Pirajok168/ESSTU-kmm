package ru.esstu.domain.modules.account.datasources.api

import ru.esstu.domain.modules.account.datasources.api.response.UserResponse

interface AccountInfoApi {


    suspend fun getUser(
        authToken: String,
         userId: String,
    ): UserResponse

}
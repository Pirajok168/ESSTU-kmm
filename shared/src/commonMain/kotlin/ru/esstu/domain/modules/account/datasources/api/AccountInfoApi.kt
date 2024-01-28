package ru.esstu.domain.modules.account.datasources.api

import ru.esstu.domain.modules.account.datasources.api.response.UserResponse
import ru.esstu.domain.utill.wrappers.Response

interface AccountInfoApi {


    suspend fun getUser(
        userId: String,
    ): Response<UserResponse>

}
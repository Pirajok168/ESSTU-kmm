package ru.esstu.features.account.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.account.data.model.UserResponse

interface AccountInfoApi {


    suspend fun getUser(
        userId: String,
    ): Response<UserResponse>

}
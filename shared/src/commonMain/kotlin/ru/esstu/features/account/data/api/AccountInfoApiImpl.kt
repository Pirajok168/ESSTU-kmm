package ru.esstu.features.account.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.account.data.model.UserResponse

class AccountInfoApiImpl(
    private val authorizedApi: AuthorizedApi
) : AccountInfoApi {
    override suspend fun getUser(userId: String): Response<UserResponse> {
        return authorizedApi.get(path = "lk/api/v2/messenger/getUserFull&id=${userId}")
    }
}
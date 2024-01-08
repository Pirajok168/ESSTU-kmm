package ru.esstu.domain.modules.account.datasources.api

import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.modules.account.datasources.api.response.UserResponse
import ru.esstu.domain.utill.wrappers.Response

class AccountInfoApiImpl(
    private val authorizedApi: AuthorizedApi
): AccountInfoApi {
    override suspend fun getUser(userId: String): Response<UserResponse> {
        return authorizedApi.get(path = "lk/api/v2/messenger/getUserFull&id=${userId}")
    }
}
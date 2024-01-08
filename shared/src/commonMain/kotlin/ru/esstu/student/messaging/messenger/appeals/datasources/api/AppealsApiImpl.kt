package ru.esstu.student.messaging.messenger.appeals.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

class AppealsApiImpl(
    private val authorizedApi: AuthorizedApi,
): AppealsApi {

    override suspend fun getAppeals(
        offset: Int,
        limit: Int
    ): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=APPEAL&offset=$offset&limit=$limit")
    }
}
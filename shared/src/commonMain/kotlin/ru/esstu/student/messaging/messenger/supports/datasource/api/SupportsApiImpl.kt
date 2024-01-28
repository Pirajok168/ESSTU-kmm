package ru.esstu.student.messaging.messenger.supports.datasource.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

class SupportsApiImpl(
    private val authorizedApi: AuthorizedApi,
): SupportsApi {
    override suspend fun getSupports(offset: Int, limit: Int): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=SUPPORT&offset=$offset&limit=$limit")
    }
}
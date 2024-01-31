package ru.esstu.features.messanger.supports.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class SupportsApiImpl(
    private val authorizedApi: AuthorizedApi,
) : SupportsApi {
    override suspend fun getSupports(offset: Int, limit: Int): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=SUPPORT&offset=$offset&limit=$limit")
    }
}
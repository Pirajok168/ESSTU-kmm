package ru.esstu.features.messanger.appeal.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class AppealsApiImpl(
    private val authorizedApi: AuthorizedApi,
) : AppealsApi {

    override suspend fun getAppeals(
        offset: Int,
        limit: Int
    ): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=APPEAL&offset=$offset&limit=$limit")
    }
}
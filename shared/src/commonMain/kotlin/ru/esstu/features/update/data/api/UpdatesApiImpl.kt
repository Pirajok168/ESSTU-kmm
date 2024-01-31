package ru.esstu.features.update.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class UpdatesApiImpl(
    private val portalApi: AuthorizedApi,
) : UpdatesApi {
    override suspend fun getUpdates(timestamp: Long): Response<DataResponse> =
        portalApi.get("lk/api/async/messenger/getGlobalUpdates?time=$timestamp")
}
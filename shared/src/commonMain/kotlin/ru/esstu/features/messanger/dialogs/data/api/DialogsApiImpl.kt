package ru.esstu.features.messanger.dialogs.data.api

import ru.esstu.data.web.api.AuthorizedApi
import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

class DialogsApiImpl(
    private val authorizedApi: AuthorizedApi,
) : DialogsApi {
    override suspend fun getDialogs(offset: Int, limit: Int): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=DIALOGUE&offset=$offset&limit=$limit")
    }
}
package ru.esstu.student.messaging.messenger.dialogs.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.ktor.AuthorizedApi
import ru.esstu.domain.utill.wrappers.Response

class DialogsApiImpl(
    private val authorizedApi: AuthorizedApi,
): DialogsApi {
    override suspend fun getDialogs(offset: Int, limit: Int): Response<DataResponse> {
        return authorizedApi.get("/lk/api/v2/messenger/getDialogs?type=DIALOGUE&offset=$offset&limit=$limit")
    }
}
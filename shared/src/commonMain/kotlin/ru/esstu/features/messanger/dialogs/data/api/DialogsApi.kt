package ru.esstu.features.messanger.dialogs.data.api


import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface DialogsApi {


    suspend fun getDialogs(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
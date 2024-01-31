package ru.esstu.features.update.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse


interface UpdatesApi {


    suspend fun getUpdates(
        timestamp: Long
    ): Response<DataResponse>
}
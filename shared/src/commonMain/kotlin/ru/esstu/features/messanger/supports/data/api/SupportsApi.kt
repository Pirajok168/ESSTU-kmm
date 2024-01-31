package ru.esstu.features.messanger.supports.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse


interface SupportsApi {
    suspend fun getSupports(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
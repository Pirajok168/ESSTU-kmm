package ru.esstu.features.messanger.appeal.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface AppealsApi {
    suspend fun getAppeals(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
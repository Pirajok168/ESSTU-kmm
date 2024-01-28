package ru.esstu.student.messaging.messenger.supports.datasource.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response


interface SupportsApi {
    suspend fun getSupports(
         offset: Int,
         limit: Int
    ): Response<DataResponse>
}
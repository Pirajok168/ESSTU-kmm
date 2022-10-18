package ru.esstu.student.messaging.messenger.supports.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface SupportsApi {

    suspend fun getSupports(
         authToken: String,
         offset: Int,
         limit: Int
    ): DataResponse
}
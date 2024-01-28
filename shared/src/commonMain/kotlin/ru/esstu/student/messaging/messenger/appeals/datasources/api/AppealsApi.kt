package ru.esstu.student.messaging.messenger.appeals.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response

interface AppealsApi {
    suspend fun getAppeals(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
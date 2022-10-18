package ru.esstu.student.messaging.messenger.appeals.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface AppealsApi  {
    companion object { const val BASE_URL = "https://esstu.ru" }


    suspend fun getAppeals(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse
}
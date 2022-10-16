package ru.esstu.student.messaging.messenger.supports.datasources.api


import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

interface SupportsApi {
    companion object { const val BASE_URL = "https://esstu.ru" }


    suspend fun getSupports(
         authToken: String,
         offset: Int,
         limit: Int
    ): DataResponse
}
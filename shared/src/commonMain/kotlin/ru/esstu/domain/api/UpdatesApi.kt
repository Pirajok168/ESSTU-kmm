package ru.esstu.domain.api

import ru.esstu.domain.response.entities.DataResponse


interface UpdatesApi {
    companion object {
        const val BASE_URL = "https://esstu.ru"
    }


    suspend fun getUpdates(
        authToken: String,
        timestamp: Long
    ): DataResponse
}
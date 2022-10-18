package ru.esstu.student.messaging.messenger.datasources.api
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface UpdatesApi {
    suspend fun getUpdates(
        authToken: String,
        timestamp: Long
    ): DataResponse
}
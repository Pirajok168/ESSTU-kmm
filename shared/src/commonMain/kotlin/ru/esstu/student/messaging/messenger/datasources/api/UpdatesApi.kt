package ru.esstu.student.messaging.messenger.datasources.api
import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

interface UpdatesApi {
    suspend fun getUpdates(
        authToken: String,
        timestamp: Long
    ): DataResponse
}
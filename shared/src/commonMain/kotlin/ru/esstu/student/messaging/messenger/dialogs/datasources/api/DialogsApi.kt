package ru.esstu.student.messaging.messenger.dialogs.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse

interface DialogsApi {



    suspend fun getDialogs(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse
}
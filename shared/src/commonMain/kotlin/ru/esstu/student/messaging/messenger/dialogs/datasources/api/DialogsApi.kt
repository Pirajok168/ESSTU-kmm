package ru.esstu.student.messaging.messenger.dialogs.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response

interface DialogsApi {



    suspend fun getDialogs(
        offset: Int,
        limit: Int
    ): Response<DataResponse>
}
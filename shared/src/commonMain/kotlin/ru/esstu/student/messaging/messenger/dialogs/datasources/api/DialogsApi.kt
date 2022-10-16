package ru.esstu.student.messaging.messenger.dialogs.datasources.api


import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse

interface DialogsApi {



    suspend fun getDialogs(
        authToken: String,
        offset: Int,
        limit: Int
    ): DataResponse
}
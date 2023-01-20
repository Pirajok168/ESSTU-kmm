package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.response.SupportGroup

interface NewSupportApi {
    suspend fun getSupport(
        authToken: String,
    ): List<SupportGroup>

    suspend fun createSupportChat(
         authToken: String,
         body: NewSupportRequestBody
    ): DataResponse


    suspend fun createSupportChatWithAttachments(
         authToken: String,
         files: List<CachedFile>,
         body: NewSupportRequestBody
    ): DataResponse
}
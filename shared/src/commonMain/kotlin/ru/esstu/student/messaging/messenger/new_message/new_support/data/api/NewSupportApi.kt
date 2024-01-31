package ru.esstu.student.messaging.messenger.new_message.new_support.data.api

import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.response.SupportGroup

interface NewSupportApi {
    suspend fun getSupport(): Response<List<SupportGroup>>

    suspend fun createSupportChat(
        body: NewSupportRequestBody
    ): Response<DataResponse>


    suspend fun createSupportChatWithAttachments(

        files: List<CachedFile>,
        body: NewSupportRequestBody
    ): Response<DataResponse>
}
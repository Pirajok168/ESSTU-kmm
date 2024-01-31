package ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api


import ru.esstu.data.web.api.model.Response
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.response.DepartmentThemeResponse


interface NewAppealApi {


    suspend fun getDepartments(): Response<List<DepartmentResponse>>


    suspend fun getDepartmentsThemes(
        code: String,
    ): Response<List<DepartmentThemeResponse>>


    suspend fun createAppealChat(
        body: NewAppealRequestBody
    ): Response<DataResponse>


    suspend fun createAppealChatWithAttachments(
        files: List<CachedFile>,
        body: NewAppealRequestBody
    ): Response<DataResponse>
}
package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentThemeResponse


interface NewAppealApi {


    suspend fun getDepartments(): Response<List<DepartmentResponse>>


    suspend fun getDepartmentsThemes(
         code: String,
    ): Response< List<DepartmentThemeResponse>>


    suspend fun createAppealChat(
       body: NewAppealRequestBody
    ): Response<DataResponse>


    suspend fun createAppealChatWithAttachments(
         files: List<CachedFile>,
         body: NewAppealRequestBody
    ): Response<DataResponse>
}
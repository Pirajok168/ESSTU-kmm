package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response.DepartmentThemeResponse


interface NewAppealApi {


    suspend fun getDepartments(
         authToken: String
    ): List<DepartmentResponse>


    suspend fun getDepartmentsThemes(
         authToken: String,
         code: String,
    ): List<DepartmentThemeResponse>


    suspend fun createAppealChat(
       authToken: String,
       body: NewAppealRequestBody
    ): DataResponse


    suspend fun createAppealChatWithAttachments(
         authToken: String,
         files: List<CachedFile>,
         body: NewAppealRequestBody
    ): DataResponse
}
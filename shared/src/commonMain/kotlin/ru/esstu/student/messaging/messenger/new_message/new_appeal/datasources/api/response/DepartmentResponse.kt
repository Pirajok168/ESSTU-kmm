package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.response

@kotlinx.serialization.Serializable
data class DepartmentResponse(
    val departmentCode: String,
    val departmentName: String,
    val departmentShortName: String?
)
package ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.response

@kotlinx.serialization.Serializable
data class DepartmentThemeResponse(
    val id: String,
    val name: String,
    val shortName: String?
)
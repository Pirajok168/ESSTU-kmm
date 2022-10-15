package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.notification_response

data class NotificationResponse(
    val createdAt: Long,
    val id: Int,
    val moduleCode: String,
    val status: String,
    val text: String,
    val type: String,
    val url: String
)
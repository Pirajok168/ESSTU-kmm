package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.request.fcm_token

data class FCMTokenRequest(
    val token: String,
    val platform: String
)
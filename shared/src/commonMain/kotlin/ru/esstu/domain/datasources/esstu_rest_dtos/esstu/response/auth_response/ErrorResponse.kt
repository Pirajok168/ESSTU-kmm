package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.auth_response



import kotlinx.serialization.SerialName

data class ErrorResponse(
    val error: String,
    @SerialName("error_description")
    val errorDescription: String
)
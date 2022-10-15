package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes

//вложения
data class FileAttachment(
    val fileCode: String,
    val fileName: String,
    val fileSize: Int,
    val id: Int,
    val type: String
)
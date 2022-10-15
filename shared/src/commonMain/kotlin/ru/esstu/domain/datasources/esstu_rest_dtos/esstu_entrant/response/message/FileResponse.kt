package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message

data class FileResponse (
    val id:Int = 0,
    val entrantId:Int = 0,
    val fileName: String? = null,
    val fileExtension: String? = null,
    val documentType: String? = null,
    val guid: String? = null
)
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response.inner_classes


data class RosCode(
    val bracing: Boolean,
    val cWork: Boolean,
    val code: String,
    val cproject: Boolean,
    val eduBlock: Int,
    val eduYear: Int,
    val elective: Boolean,
    val facultative: Boolean,
    val formOfControl: String,
    val name: String,
    val serialNumber: Int,
    val variability: Boolean,
    val weight: Double
)
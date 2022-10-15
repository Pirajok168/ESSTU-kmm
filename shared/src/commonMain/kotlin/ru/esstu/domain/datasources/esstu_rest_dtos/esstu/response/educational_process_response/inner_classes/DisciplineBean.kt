package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.educational_process_response.inner_classes


data class DisciplineBean(
    val block: String,
    val code: String,
    val cycleCode: String,
    val elective: Boolean,
    val id: Any,
    val mapDrs: Any,
    val name: String,
    val predDisciplineBean: Any,
    val refProfile: Any,
    val rosCodeList: List<RosCode>
)
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes


data class Education(
    val creationTime: Long?,
    val educationType: EducationType?,
    val employeeId: Int?,
    val fileCode: String?,
    val id: Int?,
    val institution: Institution?,
    val modificationTime: Long?,
    val note: String?,
    val number: String?,
    val qualification: Qualification?,
    val receiptDate: String?,
    val series: String?,
    val speciality: Speciality?,
    val specialityCode: String?,
    val status: String?
)
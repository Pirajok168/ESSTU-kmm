package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes


data class Employment(
    val acceptDate: String?,
    val contractEndDate: String?,
    val contractFactEndDate: String?,
    val contractInfo: String?,
    val contractStartDate: String?,
    val department: Department?,
    val dismissDate: String?,
    val employeeCode: String?,
    val employeeId: Int?,
    val post: Post?,
    val postType: String?,
    val status: String?,
    val wageRate: Double?,
    val workHours: Double?
)
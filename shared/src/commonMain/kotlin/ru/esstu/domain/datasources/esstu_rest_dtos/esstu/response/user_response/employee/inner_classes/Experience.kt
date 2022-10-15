package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes


data class Experience(
    val days: Int,
    val employeeId: Int,
    val experienceType: String,
    val months: Int,
    val years: Int
)
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes.Education
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes.Employment
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes.Experience

data class UserEmployeeResponse(
    val employeeId: Int?,
    val birthDate: Long?,
    val degrees: List<Any>?,
    val educations: List<Education>?,
    val employments: List<Employment>?,
    val experience: List<Experience>?,
    val firstName: String?,
    val fullName: String?,
    val interests: List<Any>?,
    val lastName: String?,
    val patronymic: String?,
    val photoFileCode: String?,
    val ranks: List<Any>?,
    val sex: String?
)
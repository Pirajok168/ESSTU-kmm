package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.user_response.employee.inner_classes


data class Department(
    val departmentCode: String,
    val departmentName: String,
    val departmentShortName: String,
    val departmentType: String,
    val departmentTypeName: String,
    val higherDepartmentCode: String,
    val prorectorPostCode: String?
)
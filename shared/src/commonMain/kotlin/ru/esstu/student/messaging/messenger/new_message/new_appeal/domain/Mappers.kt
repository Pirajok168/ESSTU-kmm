package ru.esstu.student.messaging.messenger.new_message.new_appeal.domain

import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.response.DepartmentResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.data.api.response.DepartmentThemeResponse
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme

fun DepartmentResponse.toAppeal() = AppealTheme(
    id = departmentCode,
    name = departmentName,
    abbreviation = departmentShortName
)

fun DepartmentThemeResponse.toAppeal() = AppealTheme(
    id = id,
    abbreviation = shortName,
    name = name
)
package ru.esstu.student.messaging.messenger.datasources


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.student.messaging.entities.*


fun UserPreview.toUser(): Sender? {
    return Sender(
        firstName = firstName ?: return null,
        lastName = lastName.orEmpty(),
        patronymic = patronymic.orEmpty(),
        photo = if (!photo.isNullOrBlank()) "https://esstu.ru/aicstorages/publicDownload/$photo" else null,
        summary = information.orEmpty(),
        id = id ?: return null
    )
}



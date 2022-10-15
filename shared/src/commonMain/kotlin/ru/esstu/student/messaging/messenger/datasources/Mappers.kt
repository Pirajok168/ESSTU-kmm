package ru.esstu.student.messaging.messenger.datasources


import ru.esstu.student.messaging.entities.*


fun ru.esstu.student.messaging.messenger.datasources.api.response.User.toUser(): User? {
    return User(
        firstName = firstName ?: return null,
        lastName = lastName.orEmpty(),
        patronymic = patronymic.orEmpty(),
        photo = if (!photo.isNullOrBlank()) "https://esstu.ru/aicstorages/publicDownload/$photo" else null,
        summary = information.orEmpty(),
        id = id ?: return null
    )
}

fun ru.esstu.student.messaging.messenger.datasources.api.response.Attachment.toAttachment(): Attachment {
    val filename = fileName.split('.').let { if (it.size > 1) it.dropLast(1) else it }.joinToString(".")
    val fileExt = fileName.split('.').let { if (it.size > 1)  it.last() else "" }

    return Attachment(
        id = id,
        type = type,
        name = filename,
        ext = fileExt,
        fileUri = if (fileCode.isNotBlank()) "https://esstu.ru/aicstorages/publicDownload/$fileCode" else "",
        size = fileSize,
        localFileUri = null
    )
}

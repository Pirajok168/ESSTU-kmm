package ru.esstu.domain.response

import ru.esstu.domain.response.entities.AttachmentResponse
import ru.esstu.domain.response.entities.UserResponse
import ru.esstu.student.news.entities.Attachment
import ru.esstu.student.news.entities.User

fun UserResponse.toUser(): User? {
    return User(
        firstName = firstName ?: return null,
        lastName = lastName.orEmpty(),
        patronymic = patronymic.orEmpty(),
        photo = if (!photo.isNullOrBlank()) "https://esstu.ru/aicstorages/publicDownload/$photo" else null,
        summary = information.orEmpty(),
        id = id ?: return null
    )
}


fun AttachmentResponse.toAttachment(): Attachment {
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
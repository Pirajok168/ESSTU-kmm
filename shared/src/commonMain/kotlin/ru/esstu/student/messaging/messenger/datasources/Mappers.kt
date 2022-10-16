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


fun ru.esstu.student.messaging.messenger.datasources.api.response.Message.toMessage(
    loadedUsers: List<ru.esstu.student.messaging.messenger.datasources.api.response.User>,
    loadedMessages: List<ru.esstu.student.messaging.messenger.datasources.api.response.Message>
): Message? {
    return Message(
        id = id,
        date = date,
        message = message.orEmpty(),
        attachments = attachments.map { attachment -> attachment.toAttachment() },
        from = loadedUsers.firstOrNull { user -> user.id == this.from }?.toUser() ?: return null,
        replyMessage = if (replyToMsgId != null) loadedMessages.firstOrNull { it.id == replyToMsgId }?.let { replyMessage ->
            ReplyMessage(
                id = replyMessage.id,
                date = date,
                message = replyMessage.message.orEmpty(),
                attachmentsCount = replyMessage.attachments.size,
                from = loadedUsers.firstOrNull { user -> user.id == replyMessage.from }?.toUser() ?: return@let null
            )
        } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}

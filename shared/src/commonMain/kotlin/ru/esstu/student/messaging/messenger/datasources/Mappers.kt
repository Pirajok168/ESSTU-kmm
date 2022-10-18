package ru.esstu.student.messaging.messenger.datasources


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachment
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

fun FileAttachment.toAttachment(): MessageAttachment {
    val filename = fileName.split('.').let { if (it.size > 1) it.dropLast(1) else it }.joinToString(".")
    val fileExt = fileName.split('.').let { if (it.size > 1)  it.last() else "" }

    return MessageAttachment(
        id = id,
        type = type,
        name = filename,
        ext = fileExt,
        fileUri = if (fileCode.isNotBlank()) "https://esstu.ru/aicstorages/publicDownload/$fileCode" else "",
        size = fileSize,
        localFileUri = null
    )
}


fun ChatMessage.toMessage(
    loadedUsers: List<UserPreview>,
    loadedMessages: List<ChatMessage>
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

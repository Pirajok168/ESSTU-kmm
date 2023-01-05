package ru.esstu.student.messaging.dialog_chat.datasources


import com.soywiz.klock.DateTime
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachmentResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated


import ru.esstu.student.messaging.entities.*

import ru.esstu.student.messaging.messenger.datasources.toUser


fun Sender.toUserEntity(): DialogChatAuthorEntity {
    return DialogChatAuthorEntity(
        summary = summary,
        photo = photo,
        lastName = lastName,
        firstName = firstName,
        patronymic = patronymic,
        id = id
    )
}

fun MessagePreview.toReplyMessage(authors: List<Sender>): ReplyMessage? {
    return ReplyMessage(
        id = id,
        from = authors.firstOrNull { it.id == from } ?: return null,
        attachmentsCount = attachments.size,
        message = message.orEmpty(),
        date = DateTime(date).unixMillisLong
    )
}

fun ReplyMessage.toDialogChatReplyMessageEntity() = DialogChatReplyMessageEntity(
    from = from.toUserEntity(),
    date = date,
    message = message,
    attachmentsCount = attachmentsCount,
    id = id
)

fun Message.toDialogChatMessageEntity(appUserId: String, dialogId: String) = DialogChatMessageEntity(
    message = message,
    opponentId = dialogId,
    appUserId = appUserId,
    from = from.toUserEntity(),
    id = id,
    date = date,
    status = status.name,
    replyMessageId = replyMessage?.id
)

fun MessageAttachment.toDialogChatAttachmentEntity(messageId: Long) = DialogChatAttachmentEntity(
    id = id,
    size = size,
    ext = ext,
    name = name,
    fileUri = fileUri,
    type = type,
    messageId = messageId,
    LocalFileUri = localFileUri,
    loadProgress = loadProgress
)

// TODO: переделать ещё историю базы данных
fun Message.toMessageWithRelated(appUserId: String, dialogId: String) = MessageWithRelated(
    message = toDialogChatMessageEntity(appUserId, dialogId),
    reply = replyMessage?.toDialogChatReplyMessageEntity(),
    attachments = attachments.map { it.toDialogChatAttachmentEntity(id) }
)

fun FileAttachmentResponse.toAttachment(): MessageAttachment {
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

fun MessagePreview.toMessage(
    authors: List<Sender>,
    replyMessages: List<ReplyMessage>
): Message? {
    return Message(
        id = id,
        date = DateTime(date).unixMillisLong,
        message = message.orEmpty(),
        attachments = attachments.map { it.toAttachment() } ,
        from = authors.firstOrNull { user -> user.id == this.from } ?: return null,
        replyMessage = if (replyToMsgId != null) replyMessages.firstOrNull { it.id == replyToMsgId } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}

fun MessageWithRelated.toMessage() = Message(
    message = message.message,
    id = message.id,
    status = enumValueOf(message.status),
    attachments = attachments.map { it.toAttachment() },
    replyMessage = reply?.toReplyMessage() ,
    date = message.date,
    from = message.from.toUser()
)

fun DialogChatAuthorEntity.toUser() = Sender(
    id = id,
    patronymic = patronymic,
    firstName = firstName,
    lastName = lastName,
    photo = photo,
    summary = summary
)

fun DialogChatAttachmentEntity.toAttachment() = MessageAttachment(
    type = type,
    fileUri = fileUri,
    name = name,
    ext = ext,
    size = size,
    id = id,
    localFileUri = LocalFileUri
)

fun DialogChatReplyMessageEntity.toReplyMessage() = ReplyMessage(
    id = id,
    attachmentsCount = attachmentsCount,
    message = message.orEmpty(),
    date = date,
    from = from.toUser()
)



suspend fun MessageResponse.toMessages(
    provideReplies: suspend (indices: List<Long>) -> List<MessagePreview>,
    provideUsers: suspend (indices: List<String>) -> List<UserPreview>,
): List<Message> {

    val existingAuthors = users.mapNotNull { it.toUser() }

    val replyIndices = messages.mapNotNull { it.replyToMsgId }
    val rawReplyMessages = provideReplies(replyIndices)
    val missingAuthorIds = rawReplyMessages.map { it.from }.distinct() - existingAuthors.map { it.id }

    val missingAuthors = if (missingAuthorIds.any())
        provideUsers(missingAuthorIds).mapNotNull { it.toUser() }
    else emptyList()

    val replyMessages = rawReplyMessages.mapNotNull { it.toReplyMessage(existingAuthors + missingAuthors) }
    return this.messages.mapNotNull { it.toMessage(authors = existingAuthors, replyMessages = replyMessages) }
}


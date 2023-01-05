package ru.esstu.student.messaging.messenger.dialogs.datasources


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.entities.*
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewLastMessage

//<editor-fold desc="DialogsApiRepositoryImpl">
fun DataResponse.toDialogs(): List<PreviewDialog> {
    return dialogs.filter { it.type == "DIALOGUE" }.mapNotNull { dialog ->
        val opponent = loadedUsers.firstOrNull { user -> user.id == dialog.peerId }?.toUser()
        PreviewDialog(
            id = dialog.peerId,
            unreadMessageCount = dialog.unreadCount,
            opponent = opponent ?: return@mapNotNull null,
            lastMessage = messages.firstOrNull { message -> message.id == dialog.lastMessageId }
                ?.toMessage(loadedUsers, messages),
            notifyAboutIt = dialog.notifySettings
        )
    }
}

fun ChatMessage.toMessage(
    loadedUsers: List<UserPreview>,
    loadedMessages: List<ChatMessage>
): PreviewLastMessage? {
    return PreviewLastMessage(
        id = id,
        date = date,
        message = message.orEmpty(),
        attachments = attachments.size,
        from = loadedUsers.firstOrNull { user -> user.id == this.from }?.toUser() ?: return null,
        replyMessage = if (replyToMsgId != null) loadedMessages.firstOrNull { it.id == replyToMsgId }
            ?.let { replyMessage ->
                ReplyMessage(
                    id = replyMessage.id,
                    date = date,
                    message = replyMessage.message.orEmpty(),
                    attachmentsCount = replyMessage.attachments.size,
                    from = loadedUsers.firstOrNull { user -> user.id == replyMessage.from }
                        ?.toUser() ?: return@let null
                )

            } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}
//</editor-fold>


//<editor-fold desc="CacheDatabase">
fun Sender.toUserEntity() = UserEntity(
    summary = summary,
    photo = photo,
    lastName = lastName,
    firstName = firstName,
    patronymic = patronymic,
    id = id
)

fun ReplyMessage.toReplyMessageEntity() = ReplyMessageEntity(
    id = id,
    from = from.toUserEntity(),
    date = date,
    message = message,
    attachmentsCount = attachmentsCount
)
//</editor-fold>


//<editor-fold desc="DialogsDbRepositoryImpl">
fun DialogWithMessage.toMessage() = PreviewDialog(
    id = dialog.id,
    opponent = dialog.opponent.toUser(),
    lastMessage = lastMessage?.toMessage(),
    notifyAboutIt = dialog.notifyAboutIt,
    unreadMessageCount = dialog.unread
)

fun UserEntity.toUser() = Sender(
    id = id,
    summary = summary, photo = photo,
    lastName = lastName, firstName = firstName, patronymic = patronymic
)

fun LastMessageWithCountAttachments.toMessage() = PreviewLastMessage(
    id = message.id,
    date = message.date,
    from = message.from.toUser(),
    message = message.message.orEmpty(),
    replyMessage = message.replyMessage?.toReplyMessage(),
    status = enumValueOf(message.status),
    attachments = attachments
)

fun ReplyMessageEntity.toReplyMessage() = ReplyMessage(
    id = id, message = message.orEmpty(), from = from.toUser(),
    date = date,
    attachmentsCount = attachmentsCount
)
//</editor-fold>

/*fun Message.toMessageEntity() = MessageEntity(
    message = message,
    date = date,
    id = id,
    from = from.toUserEntity(),
    replyMessage = replyMessage?.toReplyMessageEntity(),
    status = status.name
)

fun MessageAttachment.toAttachmentEntity(messageId: Long) = AttachmentEntity(
    messageId = messageId,
    id = id,
    size = size, ext = ext, name = name, fileUri = fileUri, type = type,
)

fun Message.toMessageWithAttachments() = LastMessageWithCountAttachments(
    message = toMessageEntity(),
    attachments = attachments.size
)

fun PreviewDialog.toDialogEntity(appUserId: String, sortOrder:Int): DialogEntity {
    return DialogEntity(
        id = id,
        unread = unreadMessageCount,
        opponent = opponent.toUserEntity(),
        notifyAboutIt = notifyAboutIt,
        appUserId = appUserId,
        lastMessageId = lastMessage?.id,
        sortOrder = sortOrder
    )
}




fun UserEntity.toUser() = Sender(
    id = id,
    summary = summary, photo = photo,
    lastName = lastName, firstName = firstName, patronymic = patronymic
)



fun AttachmentEntity.toAttachment() = MessageAttachment(
    id = id, type = type, fileUri = fileUri, name = name, ext = ext, size = size, localFileUri = null
)

fun MessageWithAttachments.toMessage() = Message(
    id = message.id,
    date = message.date,
    from = message.from.toUser(),
    message = message.message.orEmpty(),
    replyMessage = message.replyMessage?.toReplyMessage(),
    status = enumValueOf(message.status),
    attachments = attachments.map{ it.toAttachment() }
)


fun LastMessageWithCountAttachments.toMessage() = Message(
    id = message.id,
    date = message.date,
    from = message.from.toUser(),
    message = message.message.orEmpty(),
    replyMessage = message.replyMessage?.toReplyMessage(),
    status = enumValueOf(message.status),
    attachments = emptyList()
)*/




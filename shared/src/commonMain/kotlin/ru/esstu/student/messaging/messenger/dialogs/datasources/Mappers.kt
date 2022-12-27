package ru.esstu.student.messaging.messenger.dialogs.datasources


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messenger.datasources.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.AttachmentEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.datasources.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.entities.relations.DialogWithMessage



fun DataResponse.toDialogs(): List<Dialog> {
    return dialogs.filter { it.type == "DIALOGUE" }.mapNotNull { dialog ->
        val opponent = loadedUsers.firstOrNull { user -> user.id == dialog.peerId }?.toUser()
        Dialog(
            id = dialog.peerId,
            unreadMessageCount = dialog.unreadCount,
            opponent = opponent ?: return@mapNotNull null,
            lastMessage = messages.firstOrNull { message -> message.id == dialog.lastMessageId }?.toMessage(loadedUsers, messages),
            notifyAboutIt = dialog.notifySettings
        )
    }
}

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

fun Message.toMessageEntity() = MessageEntity(
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

fun Message.toMessageWithAttachments() = MessageWithAttachments(
    message = toMessageEntity(),
    attachments = attachments.map { it.toAttachmentEntity(id) }
)

fun Dialog.toDialogEntity(appUserId: String, sortOrder:Int): DialogEntity {
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


fun Dialog.toDialogWithMessage(appUserId: String, sortOrder:Int) = DialogWithMessage(
    dialog = toDialogEntity(appUserId,sortOrder),
    lastMessage = lastMessage?.toMessageWithAttachments()
)

fun UserEntity.toUser() = Sender(
    id = id,
    summary = summary, photo = photo,
    lastName = lastName, firstName = firstName, patronymic = patronymic
)

fun ReplyMessageEntity.toReplyMessage() = ReplyMessage(
    id = id, message = message.orEmpty(), from = from.toUser(),
    date = date,
    attachmentsCount = attachmentsCount
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
    attachments = attachments.map { it.toAttachment() }
)


fun DialogWithMessage.toDialogs() = Dialog(
    id = dialog.id,
    lastMessage = lastMessage?.toMessage(),
    opponent = dialog.opponent.toUser(),
    notifyAboutIt = dialog.notifyAboutIt,
    unreadMessageCount = dialog.unread
)


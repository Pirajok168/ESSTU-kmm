package ru.esstu.student.messaging.dialog_chat.datasources

import com.soywiz.klock.DateTime
import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachmentResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessagePreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message.MessageResponse
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.NewUserMessage
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_messages.entities.UserMessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAuthorTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable
import ru.esstu.student.messaging.entities.*
import ru.esstu.student.messaging.messenger.datasources.toUser
import kotlin.random.Random

//<editor-fold desc="ToEntityMapper">
fun Message.toMessageWithRelatedEntity(appUserId: String, dialogId: String) = MessageWithRelatedNew(
    message = toDialogChatMessageEntity(appUserId, dialogId),
    reply = replyMessage?.toDialogChatReplyMessageEntity(id),
    attachments = attachments.map { it.toDialogChatAttachmentEntity(id) }
)


fun Message.toDialogChatMessageEntity(appUserId: String, dialogId: String) =
    DialogChatMessageTableNew(
        message = message,
        opponentId = dialogId,
        appUserId = appUserId,
        fromSend = from.toUserEntity(),
        messageId = id,
        date = date,
        status = status.name,
        replyMessageId = replyMessage?.id
    )

fun ReplyMessage.toDialogChatReplyMessageEntity(messageId: Long) = DialogChatReplyMessageTableNew(
    fromSendReplyMessage = from.toUserEntity(),
    dateReplyMessage = date,
    messageReplayMessage = message,
    attachmentsCount = attachmentsCount,
    messageId = messageId,
    idReplyMessage = id
)

fun MessageAttachment.toDialogChatAttachmentEntity(messageId: Long) = DialogChatAttachmentTableNew(
    idAttachment = id.toLong(),
    size = size.toLong(),
    ext = ext,
    name = name,
    fileUri = fileUri,
    type = type,
    messageId = messageId,
    LocalFileUri = localFileUri,
    loadProgress = loadProgress?.toDouble()
)

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

//</editor-fold>

fun MessageWithRelatedNew.toMessage() = Message(
    message = message.message,
    id = message.messageId,
    status = enumValueOf(message.status),
    attachments = attachments.map { it.toAttachment() },
    replyMessage = reply?.toReplyMessage(),
    date = message.date,
    from = message.fromSend.toUser()
)

fun DialogChatAttachmentTableNew.toAttachment() = MessageAttachment(
    type = type,
    fileUri = fileUri,
    name = name,
    ext = ext,
    size = size.toInt(),
    id = idAttachment.toInt(),
    localFileUri = LocalFileUri
)

fun DialogChatReplyMessageTableNew.toReplyMessage() = ReplyMessage(
    id = idReplyMessage,
    attachmentsCount = attachmentsCount,
    message = messageReplayMessage.orEmpty(),
    date = dateReplyMessage,
    from = fromSendReplyMessage!!.toUser()
)

fun DialogChatAuthorEntity.toUser() = Sender(
    id = id,
    patronymic = patronymic,
    firstName = firstName,
    lastName = lastName,
    photo = photo,
    summary = summary
)


//<editor-fold desc="Opponent">
fun DialogChatAuthorTableNew.toUser() = Sender(
    id = id,
    patronymic = patronymic,
    firstName = fitstName,
    lastName = lastName,
    photo = photo,
    summary = summary
)

fun Sender.toUserEntityOpponent(): DialogChatAuthorTableNew {
    return DialogChatAuthorTableNew(
        summary = summary,
        photo = photo.orEmpty(),
        lastName = lastName,
        fitstName = firstName,
        patronymic = patronymic,
        id = id
    )
}
//</editor-fold>


fun UserMessageWithRelatedNew.toSentUserMessage() = NewUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    text = message.text,
)

fun UserCachedFileTable.toCachedFile(
    fileSystem: FileSystem = storage().fileSystem
): CachedFile {

    fileSystem.write("${producePath().path}/$name.$ext".toPath(), true) {
        write(source)
    }



    return CachedFile(
        type = type,
        name = name,
        ext = ext,
        size = size,
        sourceFile = "${producePath().path}/$name.$ext",
        uri = "${producePath().path}/$name.$ext"
    )
}

fun NewUserMessage.toUserMessageEntity(
    appUserId: String,
    dialogId: String
): UserMessageEntityTable {
    return UserMessageEntityTable(
        appUserId = appUserId,
        dialogId = dialogId,
        text = text,
        replyMessageId = replyMessage?.id,
    )
}

fun CachedFile.toEntity(
    appUserId: String,
    dialogId: String,
    fileSystem: FileSystem = storage().fileSystem,
): UserCachedFileTable {


    return UserCachedFileTable(
        dialogId = dialogId,
        appUserId = appUserId,
        source = fileSystem.read(sourceFile.toPath()) {
            readByteArray()
        },
        size = size,
        ext = ext,
        name = name,
        type = type,
        idCached = Random.nextLong().toInt()
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

fun MessagePreview.toMessage(
    authors: List<Sender>,
    replyMessages: List<ReplyMessage>
): Message? {
    return Message(
        id = id,
        date = DateTime(date).unixMillisLong,
        message = message.orEmpty(),
        attachments = attachments.map { it.toAttachment() },
        from = authors.firstOrNull { user -> user.id == this.from } ?: return null,
        replyMessage = if (replyToMsgId != null) replyMessages.firstOrNull { it.id == replyToMsgId } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}

fun FileAttachmentResponse.toAttachment(): MessageAttachment {
    val filename =
        fileName.split('.').let { if (it.size > 1) it.dropLast(1) else it }.joinToString(".")
    val fileExt = fileName.split('.').let { if (it.size > 1) it.last() else "" }

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

suspend fun MessageResponse.toMessages(
    provideReplies: suspend (indices: List<Long>) -> List<MessagePreview>,
    provideUsers: suspend (indices: List<String>) -> List<UserPreview>,
): List<Message> {

    val existingAuthors = users.mapNotNull { it.toUser() }

    val replyIndices = messages.mapNotNull { it.replyToMsgId }
    val rawReplyMessages = provideReplies(replyIndices)
    val missingAuthorIds =
        rawReplyMessages.map { it.from }.distinct() - existingAuthors.map { it.id }

    val missingAuthors = if (missingAuthorIds.any())
        provideUsers(missingAuthorIds).mapNotNull { it.toUser() }
    else emptyList()

    val replyMessages =
        rawReplyMessages.mapNotNull { it.toReplyMessage(existingAuthors + missingAuthors) }
    return this.messages.mapNotNull {
        it.toMessage(
            authors = existingAuthors,
            replyMessages = replyMessages
        )
    }
}





package ru.esstu.student.messaging.dialog_chat_new.datasources

import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.relations.UserMessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.toCachedFile
import ru.esstu.student.messaging.dialog_chat.datasources.toMessage
import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.NewUserMessage
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.user_messages.entities.UserMessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAuthorTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender
import kotlin.random.Random

//<editor-fold desc="ToEntityMapper">
fun Message.toMessageWithRelatedEntity(appUserId: String, dialogId: String) = MessageWithRelatedNew(
    message = toDialogChatMessageEntity(appUserId, dialogId),
    reply = replyMessage?.toDialogChatReplyMessageEntity(id),
    attachments = attachments.map { it.toDialogChatAttachmentEntity(id) }
)


fun Message.toDialogChatMessageEntity(appUserId: String, dialogId: String) = DialogChatMessageTableNew(
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

fun  MessageWithRelatedNew.toMessage() = Message(
    message = message.message,
    id = message.messageId,
    status = enumValueOf(message.status),
    attachments = attachments.map { it.toAttachment() },
    replyMessage = reply?.toReplyMessage() ,
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

    fileSystem.write("${producePath().path}/$name.$ext".toPath(), true){
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

fun NewUserMessage.toUserMessageEntity(appUserId: String, dialogId: String): UserMessageEntityTable {
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
        source = fileSystem.read(sourceFile.toPath()){
            readByteArray()
        },
        size = size,
        ext = ext,
        name = name,
        type = type,
        idCached = Random.nextLong().toInt()
    )
}





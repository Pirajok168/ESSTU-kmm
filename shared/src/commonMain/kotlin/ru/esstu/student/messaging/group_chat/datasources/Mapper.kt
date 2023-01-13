package ru.esstu.student.messaging.group_chat.datasources


import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.domain.modules.account.datasources.datastore.producePath
import ru.esstu.domain.modules.account.datasources.datastore.storage
import ru.esstu.student.messaging.entities.*
import ru.esstu.student.messaging.group_chat.datasources.api.response.ConversationResponse
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.datasources.db.erred_messages.entities.GroupChatErredMessageWithRelated
import ru.esstu.student.messaging.group_chat.datasources.db.header.entities.ConversationWithParticipants
import ru.esstu.student.messaging.group_chat.datasources.db.user_messages.entities.GroupChatUserMessageWithRelated
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredCachedFile
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredMessage
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatParticipant
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserCachedFileEntity
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserMessage
import ru.esstu.student.messaging.messenger.datasources.toUser
import kotlin.random.Random

fun ConversationResponse.toConversation(): Conversation {
    val participants = users.mapNotNull { it.toUser() }
    val author = participants.firstOrNull { it.id == conversation.creatorId }
    return Conversation(
        conversation.id,
        author = author,
        notifyAboutIt = notifySettings,
        title = conversation.name,
        participants = participants,
    )
}

fun GroupChatAuthorEntity.toUser() = Sender(
    id = id,
    patronymic = patronymic,
    firstName = firstName,
    lastName = lastName,
    photo = photo,
    summary = summary
)

fun GroupChatAttachment.toAttachment() = MessageAttachment(
    type = type,
    fileUri = fileUri,
    name = name,
    ext = ext,
    size = size.toInt(),
    id = idAttachment.toInt(),
    localFileUri = LocalFileUri
)

fun GroupChatReplyMessage.toReplyMessage() = ReplyMessage(
    id = idReplyMessage,
    attachmentsCount = attachmentsCount,
    message = messageReplayMessage.orEmpty(),
    date = dateReplyMessage,
    from = fromSendReplyMessage?.toUser()!!
)

fun MessageGroupChatWithRelated.toMessage() = Message(
    message = message.message,
    id = message.idGroupChatMessage,
    status = enumValueOf(message.status),
    attachments = attachments.map { it.toAttachment() },
    replyMessage = reply?.toReplyMessage(),
    date = message.date,
    from = message.fromGroup.toUser()
)

fun Sender.toUserEntity(): GroupChatAuthorEntity {
    return GroupChatAuthorEntity(
        summary = summary,
        photo = photo,
        lastName = lastName,
        firstName = firstName,
        patronymic = patronymic,
        id = id
    )
}

fun Message.toDialogChatMessageEntity(appUserId: String, conversationId: Int) = GroupChatMessage(
    message = message,
    conversationId = conversationId.toLong(),
    appUserId = appUserId,
    fromGroup = from.toUserEntity(),
    idGroupChatMessage = id,
    date = date,
    status = status.name,
    replyMessageId = replyMessage?.id
)

fun ReplyMessage.toDialogChatReplyMessageEntity(conversationId: Long) = GroupChatReplyMessage(
    fromSendReplyMessage = from.toUserEntity(),
    dateReplyMessage = date,
    messageReplayMessage = message,
    attachmentsCount = attachmentsCount,
    idReplyMessage = id,
    messageIdReply = conversationId
)
fun MessageAttachment.toDialogChatAttachmentEntity(messageId: Long) = GroupChatAttachment(
    idAttachment = id.toLong(),
    size = size.toLong(),
    ext = ext,
    name = name,
    fileUri = fileUri,
    type = type,
    messageIdAttachment = messageId,
    LocalFileUri = localFileUri,
    loadProgress = loadProgress?.toDouble()
)

fun Message.toMessageWithRelatedGroupChat(appUserId: String, conversationId: Int) =
    MessageGroupChatWithRelated(
    message = toDialogChatMessageEntity(appUserId, conversationId),
    reply = replyMessage?.toDialogChatReplyMessageEntity(id),
    attachments = attachments.map { it.toDialogChatAttachmentEntity(id) }
)

fun GroupChatParticipant.toUser() = Sender(
    id = idParticipant,
    summary = summary.orEmpty(),
    photo = photo,
    lastName = lastName.orEmpty(),
    firstName = firstName.orEmpty(),
    patronymic = patronymic.orEmpty()
)

fun ConversationWithParticipants.toConversation() = Conversation(
    id = conversation.id.toInt(),
    participants = participants.map{ it.toUser() },
    author = conversation.author?.toUser(),
    notifyAboutIt = conversation.notifyAboutIt,
    title = conversation.title,
)
fun Conversation.toConversationEntity(appUserId: String) = GroupChatConversation(
    appUserId = appUserId,
    title = title,
    notifyAboutIt = notifyAboutIt,
    id = id.toLong(),
    author = author?.toUserEntity()
)

fun Sender.toParticipantEntity(conversationId: Int, appUserId: String) = GroupChatParticipant(
    appUserId = appUserId,
    idParticipant = id,
    conversationId = conversationId.toLong(),
    patronymic = patronymic, firstName = firstName,
    lastName = lastName, photo = photo, summary = summary,
)

fun Conversation.toConversationWithParticipantsEntity(appUserId: String): ConversationWithParticipants {
    return ConversationWithParticipants(
        conversation = toConversationEntity(appUserId = appUserId),
        participants = participants.map { it.toParticipantEntity(id, appUserId) }
    )
}

fun NewUserMessage.toUserMessageEntity(appUserId:String, convId: Int): GroupChatUserMessage {
    return GroupChatUserMessage(
        appUserId = appUserId,
        conversationId = convId.toLong(),
        text = text,
        replyMessageId = replyMessage?.id,
    )
}

fun CachedFile.toEntity(
    appUserId: String, convId: Int,   fileSystem: FileSystem = storage().fileSystem
): GroupChatUserCachedFileEntity {


    return GroupChatUserCachedFileEntity(
        conversationId = convId.toLong(),
        appUserId = appUserId,
        source =  fileSystem.read(sourceFile.toPath()) {
            readByteArray()
        },
        size = size,
        ext = ext,
        name = name,
        type = type,
        idCached = Random.nextInt(),
        sourceFile = sourceFile
    )
}

fun GroupChatUserMessageWithRelated.toSentUserMessage() = NewUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    text = message.text,
)

fun GroupChatUserCachedFileEntity.toCachedFile(
    fileSystem: FileSystem = storage().fileSystem
): CachedFile {

  /*  fileSystem.write("${producePath().path}/$name.$ext".toPath(), false) {
        write(source)
    }*/

    return CachedFile(
        type = type,
        name = name,
        ext = ext,
        size = size,
        sourceFile = sourceFile.orEmpty(),
        uri =  sourceFile.orEmpty()
    )
}

fun  GroupChatErredCachedFile.toCachedFile(): CachedFile {



    return CachedFile(
        type = type,
        name = name,
        ext = ext,
        size = size,
        sourceFile = sourceFile.orEmpty(),
        uri = sourceFile.orEmpty()
    )
}


fun GroupChatErredMessageWithRelated.toSentUserMessage() = SentUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    id = message.idErredMessage,
    text = message.text,
    status = DeliveryStatus.ERRED,
    date = message.date
)

fun SentUserMessage.toErredMessageEntity(appUserId:String, convId: Int): GroupChatErredMessage? {
    if(status!=DeliveryStatus.ERRED) return null
    return GroupChatErredMessage(
        appUserId = appUserId,
        convId = convId.toLong(),
        idErredMessage = id,
        text = text,
        replyMessageId = replyMessage?.id,
        date = date
    )
}

fun CachedFile.toEntity(messageId: Long): GroupChatErredCachedFile {



    return GroupChatErredCachedFile(
        messageId = messageId,
        sourceFile = sourceFile,
        size = size,
        ext = ext,
        name = name,
        type = type,
        idCahedFile = Random.nextInt()
    )
}

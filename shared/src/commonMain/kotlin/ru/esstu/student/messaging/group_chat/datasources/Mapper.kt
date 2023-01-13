package ru.esstu.student.messaging.group_chat.datasources


import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.group_chat.datasources.api.response.ConversationResponse
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.group_chat.datasources.db.header.entities.ConversationWithParticipants
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatParticipant
import ru.esstu.student.messaging.messenger.datasources.toUser

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
package ru.esstu.android.student.messaging.group_chat.datasources

import org.joda.time.LocalDateTime
import ru.esstu.android.student.messaging.entities.*
import ru.esstu.android.student.messaging.group_chat.datasources.api.response.ConversationResponse
import ru.esstu.android.student.messaging.group_chat.datasources.api.response.MessageResponse
import ru.esstu.android.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatAttachmentEntity
import ru.esstu.android.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.android.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatMessageEntity
import ru.esstu.android.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatReplyMessageEntity
import ru.esstu.android.student.messaging.group_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.android.student.messaging.group_chat.entities.Conversation
import ru.esstu.android.student.messaging.group_chat.util.toReplyMessage
import ru.esstu.android.student.messaging.messenger.datasources.toAttachment
import ru.esstu.android.student.messaging.messenger.datasources.toUser
import java.util.*

fun User.toUserEntity(): GroupChatAuthorEntity {
    return GroupChatAuthorEntity(
        summary = summary,
        photo = photo,
        lastName = lastName,
        firstName = firstName,
        patronymic = patronymic,
        id = id
    )
}

fun ReplyMessage.toDialogChatReplyMessageEntity() = GroupChatReplyMessageEntity(
    from = from.toUserEntity(),
    date = date.toDate().time,
    message = message,
    attachmentsCount = attachmentsCount,
    id = id
)

fun Message.toDialogChatMessageEntity(appUserId: String, conversationId: Int) = GroupChatMessageEntity(
    message = message,
    conversationId = conversationId,
    appUserId = appUserId,
    from = from.toUserEntity(),
    id = id,
    date = date.toDate().time,
    status = status.name,
    replyMessageId = replyMessage?.id
)

fun Attachment.toDialogChatAttachmentEntity(messageId: Long) = GroupChatAttachmentEntity(
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

fun Message.toMessageWithRelated(appUserId: String, conversationId: Int) = MessageWithRelated(
    message = toDialogChatMessageEntity(appUserId, conversationId),
    reply = replyMessage?.toDialogChatReplyMessageEntity(),
    attachments = attachments.map { it.toDialogChatAttachmentEntity(id) }
)

fun GroupChatAuthorEntity.toUser() = User(
    id = id,
    patronymic = patronymic,
    firstName = firstName,
    lastName = lastName,
    photo = photo,
    summary = summary
)

fun GroupChatAttachmentEntity.toAttachment() = Attachment(
    type = type,
    fileUri = fileUri,
    name = name,
    ext = ext,
    size = size,
    id = id,
    localFileUri = LocalFileUri
)

fun GroupChatReplyMessageEntity.toReplyMessage() = ReplyMessage(
    id = id,
    attachmentsCount = attachmentsCount,
    message = message.orEmpty(),
    date = LocalDateTime.fromDateFields(Date(date)),
    from = from.toUser()
)

fun MessageWithRelated.toMessage() = Message(
    message = message.message,
    id = message.id,
    status = enumValueOf(message.status),
    attachments = attachments.map { it.toAttachment() },
    replyMessage = reply?.toReplyMessage(),
    date = LocalDateTime.fromDateFields(Date(message.date)),
    from = message.from.toUser()
)

fun ru.esstu.android.student.messaging.messenger.datasources.api.response.Message.toReplyMessage(authors: List<User>): ReplyMessage? {
    return ReplyMessage(
        id = id,
        from = authors.firstOrNull { it.id == from } ?: return null,
        attachmentsCount = attachments.size,
        message = message.orEmpty(),
        date = LocalDateTime.fromDateFields(Date(date))
    )
}

fun ru.esstu.android.student.messaging.messenger.datasources.api.response.Message.toMessage(
    authors: List<User>,
    replyMessages: List<ReplyMessage>
): Message? {
    return Message(
        id = id,
        date = LocalDateTime.fromDateFields(Date(date)),
        message = message.orEmpty(),
        attachments = attachments.map { attachment -> attachment.toAttachment() },
        from = authors.firstOrNull { user -> user.id == this.from } ?: return null,
        replyMessage = if (replyToMsgId != null) replyMessages.firstOrNull { it.id == replyToMsgId } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}

fun MessageResponse.toMessages(replyMessages: List<Message> = emptyList()): List<Message> {
    val users = users.mapNotNull { it.toUser() }
    return messages.mapNotNull { message ->
        message.run {
            Message(
                id = id,
                date = LocalDateTime.fromDateFields(Date(date)),
                message = message.message.orEmpty(),
                attachments = attachments.map { attachment -> attachment.toAttachment() },
                from = users.firstOrNull { user -> user.id == this.from } ?: return@mapNotNull null,
                replyMessage = if (replyToMsgId != null) replyMessages.firstOrNull { it.id == replyToMsgId }?.toReplyMessage() else null,
                status = if (message.views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
            )
        }
    }
}

suspend fun MessageResponse.toMessages(
    provideReplies: suspend (indices: List<Long>) -> List<ru.esstu.android.student.messaging.messenger.datasources.api.response.Message>,
    provideUsers: suspend (indices: List<String>) -> List<ru.esstu.android.student.messaging.messenger.datasources.api.response.User>,
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

fun ConversationResponse.toConversation(): Conversation? {
    val participants = users.mapNotNull { it.toUser() }
    val author = participants.firstOrNull { it.id == conversation.creatorId } ?: return null
    return Conversation(
        conversation.id,
        author = author,
        notifyAboutIt = notifySettings,
        title = conversation.name,
        participants = participants,
    )
}


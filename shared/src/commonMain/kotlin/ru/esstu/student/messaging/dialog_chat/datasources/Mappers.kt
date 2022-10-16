package ru.esstu.student.messaging.dialog_chat.datasources


import com.soywiz.klock.DateTime
import ru.esstu.student.messaging.dialog_chat.datasources.api.response.MessageResponse

import ru.esstu.student.messaging.entities.*
import ru.esstu.student.messaging.messenger.datasources.toAttachment

import ru.esstu.student.messaging.messenger.datasources.toUser

fun ru.esstu.student.messaging.messenger.datasources.api.response.Message.toReplyMessage(authors: List<User>): ReplyMessage? {
    return ReplyMessage(
        id = id,
        from = authors.firstOrNull { it.id == from } ?: return null,
        attachmentsCount = attachments.size,
        message = message.orEmpty(),
        date = DateTime(date).unixMillisLong
    )
}

fun ru.esstu.student.messaging.messenger.datasources.api.response.Message.toMessage(
    authors: List<User>,
    replyMessages: List<ReplyMessage>
): Message? {
    return Message(
        id = id,
        date = DateTime(date).unixMillisLong,
        message = message.orEmpty(),
        attachments = attachments.map { attachment -> attachment.toAttachment() },
        from = authors.firstOrNull { user -> user.id == this.from } ?: return null,
        replyMessage = if (replyToMsgId != null) replyMessages.firstOrNull { it.id == replyToMsgId } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}



suspend fun MessageResponse.toMessages(
    provideReplies: suspend (indices: List<Long>) -> List<ru.esstu.student.messaging.messenger.datasources.api.response.Message>,
    provideUsers: suspend (indices: List<String>) -> List<ru.esstu.student.messaging.messenger.datasources.api.response.User>,
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


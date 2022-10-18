package ru.esstu.student.messaging.dialog_chat.util

import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.NewUserMessage
import ru.esstu.student.messaging.dialog_chat.entities.SentUserMessage
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.ReplyMessage
import kotlin.random.Random

fun CachedFile.toAttachment() = MessageAttachment(
    id = Random.nextInt(),
    type = type,
    fileUri = uri,
    localFileUri = uri,
    name = name,
    ext = ext,
    size = size.toInt()
)

fun NewUserMessage.toSentUserMessage() = SentUserMessage(
    text = text,
    attachments = attachments,
    replyMessage = replyMessage
)

fun Message.toReplyMessage() = ReplyMessage(
    date = date,
    message = message,
    from = from,
    id = id,
    attachmentsCount = attachments.size
)


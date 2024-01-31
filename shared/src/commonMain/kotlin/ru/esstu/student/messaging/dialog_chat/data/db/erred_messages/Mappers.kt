package ru.esstu.student.messaging.dialog_chat.data.db.erred_messages

import okio.FileSystem
import okio.Path.Companion.toPath
import ru.esstu.data.fileSystem.producePath
import ru.esstu.data.fileSystem.storage
import ru.esstu.student.messaging.dialog_chat.data.db.erred_messages.entities.ErredMessageWithRelatedNew
import ru.esstu.student.messaging.dialog_chat.domain.toMessage
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredMessageTableNew
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.SentUserMessage
import kotlin.random.Random


fun ErredCachedFileTableNew.toCachedFile(
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

fun ErredMessageWithRelatedNew.toSentUserMessage() = SentUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    id = message.idErredMessage,
    text = message.text,
    status = DeliveryStatus.ERRED,
    date = message.date
)

fun SentUserMessage.toErredMessageEntity(
    appUserId: String,
    dialogId: String
): ErredMessageTableNew? {
    if (status != DeliveryStatus.ERRED) return null
    return ErredMessageTableNew(
        appUserId = appUserId,
        dialogId = dialogId,
        idErredMessage = id,
        text = text,
        replyMessageId = replyMessage?.id,
        date = date
    )
}

fun CachedFile.toEntity(
    messageId: Long,
    fileSystem: FileSystem = storage().fileSystem
): ErredCachedFileTableNew {


    return ErredCachedFileTableNew(
        messageId = messageId,
        source = fileSystem.read(sourceFile.toPath()) {
            readByteArray()
        },
        size = size,
        ext = ext,
        name = name,
        type = type,
        idCahedFile = Random.nextInt()
    )
}


package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages


import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.relations.ErredMessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.toMessage
import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.SentUserMessage
import ru.esstu.student.messaging.entities.DeliveryStatus

fun CachedFile.toEntity(messageId: Long): ErredCachedFileEntity {

   /* val sourceBytes = FileInputStream(source).use { fs ->
        fs.readBytes()
    }
*/
    return ErredCachedFileEntity(
        messageId = messageId,
        source = null,
        size = size,
        ext = ext,
        name = name,
        type = type,
    )
}

fun ErredCachedFileEntity.toCachedFile(): CachedFile {

    /*val cachedFile = File(context.cacheDir, "$name.$ext")

    FileOutputStream(cachedFile).use { cachedFileStream ->
        cachedFileStream.write(source)
    }*/

    return CachedFile(
        type = type,
        name = name,
        ext = ext,
        size = size,
        sourceFile = "",
        uri = ""
    )
}

fun SentUserMessage.toErredMessageEntity(appUserId:String, dialogId:String): ErredMessageEntity? {
    if(status!=DeliveryStatus.ERRED) return null
    return ErredMessageEntity(
        appUserId = appUserId,
        dialogId = dialogId,
        id = id,
        text = text,
        replyMessageId = replyMessage?.id,
        date = date
    )
}


fun ErredMessageWithRelated.toSentUserMessage() = SentUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    id = message.id,
    text = message.text,
    status = DeliveryStatus.ERRED,
    date = message.date
)

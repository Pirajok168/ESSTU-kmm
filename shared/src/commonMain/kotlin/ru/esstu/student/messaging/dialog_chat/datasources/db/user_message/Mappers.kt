package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message


import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.relations.UserMessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.toMessage
import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.NewUserMessage
import kotlin.random.Random


fun CachedFile.toEntity(appUserId: String, dialogId: String): UserCachedFileEntity {


    return UserCachedFileEntity(
        dialogId = dialogId,
        appUserId = appUserId,
        source = sourceFile,
        size = size,
        ext = ext,
        name = name,
        type = type,
        id = Random.nextLong()
    )
}

fun UserCachedFileEntity.toCachedFile(): CachedFile {

    /*val cachedFile = File(context.cacheDir, "$name.$ext")

    FileOutputStream(cachedFile).use { cachedFileStream ->
        cachedFileStream.write(source)
    }*/

    return CachedFile(
        type = type,
        name = name,
        ext = ext,
        size = size,
        sourceFile = source,
        uri = source
    )
}

fun NewUserMessage.toUserMessageEntity(appUserId: String, dialogId: String): UserMessageEntity {
    return UserMessageEntity(
        appUserId = appUserId,
        dialogId = dialogId,
        text = text,
        replyMessageId = replyMessage?.id,
    )
}


fun UserMessageWithRelated.toSentUserMessage() = NewUserMessage(
    attachments = attachments.map { it.toCachedFile() },
    replyMessage = reply?.toMessage(),
    text = message.text,
)
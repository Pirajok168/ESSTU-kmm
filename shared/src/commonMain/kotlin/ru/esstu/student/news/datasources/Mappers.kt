package ru.esstu.student.news.datasources

import com.soywiz.klock.DateTime
import ru.esstu.student.news.announcement.datasources.db.timestamp.entities.TimestampEntity
import ru.esstu.student.news.datasources.relations.NewsWithAttachments
import ru.esstu.student.news.entities.Attachment
import ru.esstu.student.news.entities.NewsNode
import ru.esstu.student.news.entities.User

fun NewsAttachmentEntity.toAttachment() = Attachment(
    type = type, id = idAttachment, name = name, loadProgress = loadProgress, localFileUri = localFileUri, size = size, ext = ext, fileUri = fileUri
)

fun UserEntity.toUser() = User(
    id = id, patronymic = patronymic, firstName = firstName, lastName = lastName, photo = photo, summary = summary
)

fun NewsWithAttachments.toNews() = NewsNode(
    id = news.id,
    date = DateTime(news.date),
    message = news.message,
    title = news.title,
    from = news.from.toUser(),
    attachments = attachments.map { it.toAttachment() }
)

fun TimestampEntity.toTimeStamp() = timestamp

fun Long.toTimeStampEntity(appUserId:String) = TimestampEntity(
    appUserId = appUserId,
    timestamp = this
)
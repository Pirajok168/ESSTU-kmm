package ru.esstu.student.news.announcement.db.announcement

import com.soywiz.klock.DateTime
import ru.esstu.student.news.announcement.datasources.db.timestamp.entities.TimestampEntity
import ru.esstu.student.news.announcement.db.announcement.entities.NewsAttachmentEntity
import ru.esstu.student.news.announcement.db.announcement.entities.UserEntity
import ru.esstu.student.news.announcement.db.announcement.entities.relations.NewsWithAttachments
import ru.esstu.student.news.entities.AttachmentNews
import ru.esstu.student.news.entities.NewsNode
import ru.esstu.student.news.entities.Creator

fun NewsAttachmentEntity.toAttachment() = AttachmentNews(
    type = type, id = idAttachment, name = name, loadProgress = loadProgress, localFileUri = localFileUri, size = size, ext = ext, fileUri = fileUri
)

fun UserEntity.toUser() = Creator(
    id = id,
    firstName = firstName,
    lastName = lastName,
    patronymic = patronymic,
    summary = summary,
    photo = photo
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
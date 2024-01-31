package ru.esstu.features.news.announcement.data.db

import kotlinx.datetime.Instant
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.features.news.announcement.data.db.entities.NewsAttachmentEntity
import ru.esstu.features.news.announcement.data.db.entities.UserEntity
import ru.esstu.features.news.announcement.data.db.entities.relations.NewsWithAttachments
import ru.esstu.features.news.announcement.domain.model.AttachmentNews
import ru.esstu.features.news.announcement.domain.model.Creator
import ru.esstu.features.news.announcement.domain.model.NewsNode

fun NewsAttachmentEntity.toAttachment() = AttachmentNews(
    type = type,
    id = idAttachment,
    name = name,
    loadProgress = loadProgress,
    localFileUri = localFileUri,
    size = size,
    ext = ext,
    fileUri = fileUri
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
    date = Instant.fromEpochMilliseconds(news.date).toLocalDateTime(),
    message = news.message,
    title = news.title,
    from = news.from.toUser(),
    attachments = attachments.map { it.toAttachment() }
)

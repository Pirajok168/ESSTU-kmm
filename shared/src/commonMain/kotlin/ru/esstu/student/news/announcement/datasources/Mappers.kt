package ru.esstu.student.news.announcement.datasources

import kotlinx.datetime.Instant
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachmentResponse
import ru.esstu.domain.utill.workingDate.toInstant
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.student.news.announcement.db.announcement.entities.NewsAttachmentEntity
import ru.esstu.student.news.announcement.db.announcement.entities.NewsEntity
import ru.esstu.student.news.announcement.db.announcement.entities.UserEntity
import ru.esstu.student.news.announcement.db.announcement.entities.relations.NewsWithAttachments
import ru.esstu.student.news.entities.AttachmentNews
import ru.esstu.student.news.entities.Creator
import ru.esstu.student.news.entities.NewsNode


fun UserPreview.toUser(): Creator? {
    return Creator(
        id = id ?: return null,
        firstName = firstName ?: return null,
        lastName = lastName.orEmpty(),
        patronymic = patronymic.orEmpty(),
        summary = information.orEmpty(),
        photo = if (!photo.isNullOrBlank()) "https://esstu.ru/aicstorages/publicDownload/$photo" else null
    )
}


fun FileAttachmentResponse.toAttachment(): AttachmentNews {
    val filename = fileName.split('.').let { if (it.size > 1) it.dropLast(1) else it }.joinToString(".")
    val fileExt = fileName.split('.').let { if (it.size > 1)  it.last() else "" }

    return AttachmentNews(
        id = id,
        type = type,
        name = filename,
        ext = fileExt,
        fileUri = if (fileCode.isNotBlank()) "https://esstu.ru/aicstorages/publicDownload/$fileCode" else "",
        size = fileSize,
        localFileUri = null
    )
}


fun DataResponse.toAnnouncements(): List<NewsNode> {

    return conversations.filter { it.type == "ANNOUNCEMENT" }.mapNotNull { conversation ->
        val creator = loadedUsers.firstOrNull { it.id == conversation.creatorId }?.toUser() ?: return@mapNotNull null
        val messageId = dialogs.firstOrNull { it.peerId == conversation.id.toString() }?.lastMessageId
        val message = messages.firstOrNull { it.id == messageId }
        val attachments = message?.attachments?.map { it.toAttachment() }.orEmpty()

        NewsNode(
            id = conversation.id.toLong(),
            title = conversation.name,
            from = creator,
            attachments = attachments,
            message = message?.message.orEmpty(),
            date = Instant.fromEpochMilliseconds(conversation.date).toLocalDateTime()
        )
    }
}

fun Creator.toUserEntity() = UserEntity(
    id = id, summary = summary, photo = photo, lastName = lastName, firstName = firstName, patronymic = patronymic
)

fun AttachmentNews.toNewsAttachmentEntity(newsId: Long) = NewsAttachmentEntity(
    idAttachment = id, fileUri = fileUri, ext = ext, size = size, name = name, type = type, newsId = newsId, loadProgress = loadProgress, localFileUri = localFileUri
)

fun NewsNode.toNewsWithAttachments() = NewsWithAttachments(
    news = NewsEntity(
        id = id,
        from = from.toUserEntity(),
        title = title, message = message, date = date.toInstant().epochSeconds
    ),
    attachments = attachments.map { it.toNewsAttachmentEntity(id) }
)

package ru.esstu.news.announcement.domain.entities

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachmentResponse



data class NewsNode(
    val id: Long,
    val from: Creator,
    val date: LocalDateTime,
    val title:String,
    val message: String,
    val attachments: List<AttachmentNews> = emptyList()
)

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
            date = Instant.fromEpochMilliseconds(conversation.date).toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}

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
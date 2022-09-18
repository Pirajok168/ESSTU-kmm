package ru.esstu.student.news.announcement.datasources

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.locale.russian
import ru.esstu.domain.response.entities.DataResponse
import ru.esstu.domain.response.toAttachment
import ru.esstu.domain.response.toUser
import ru.esstu.student.news.datasources.NewsAttachmentEntity
import ru.esstu.student.news.datasources.NewsEntity
import ru.esstu.student.news.datasources.UserEntity
import ru.esstu.student.news.datasources.relations.NewsWithAttachments
import ru.esstu.student.news.entities.Attachment
import ru.esstu.student.news.entities.NewsNode
import ru.esstu.student.news.entities.User

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
            date = DateTime(conversation.date)
        )
    }
}

fun User.toUserEntity() = UserEntity(
    id = id, summary = summary, photo = photo, lastName = lastName, firstName = firstName, patronymic = patronymic
)

fun Attachment.toNewsAttachmentEntity(newsId: Long) = NewsAttachmentEntity(
    idAttachment = id, fileUri = fileUri, ext = ext, size = size, name = name, type = type, newsId = newsId, loadProgress = loadProgress, localFileUri = localFileUri
)

fun NewsNode.toNewsWithAttachments() = NewsWithAttachments(
    news = NewsEntity(
        id = id,
        from = from.toUserEntity(),
        title = title, message = message, date = date.unixMillisLong
    ),
    attachments = attachments.map { it.toNewsAttachmentEntity(id) }
)

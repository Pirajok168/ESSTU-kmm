package ru.esstu.student.news.announcement.datasources

import com.soywiz.klock.DateTime
import ru.esstu.domain.response.entities.DataResponse
import ru.esstu.domain.response.toAttachment
import ru.esstu.domain.response.toUser
import ru.esstu.student.news.entities.NewsNode

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
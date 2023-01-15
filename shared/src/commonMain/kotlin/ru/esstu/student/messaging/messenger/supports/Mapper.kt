package ru.esstu.student.messaging.messenger.supports

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messenger.conversations.datasources.db.entities.ConversationWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.toMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUser
import ru.esstu.student.messaging.messenger.supports.datasource.db.entities.SupportWithMessage

fun DataResponse.toSupports(): List<ConversationPreview> {
    return dialogs.filter { dialog -> dialog.type == "SUPPORT" }.mapNotNull {
        val rawConv = conversations.firstOrNull { conv -> conv.id.toString() == it.peerId } ?: return@mapNotNull null
        val author = loadedUsers.firstOrNull { user -> user.id == rawConv.creatorId }?.toUser()
        val laseMessage = messages.firstOrNull { message -> message.id == it.lastMessageId }
        ConversationPreview(
            id = rawConv.id,
            title = rawConv.name,
            notifyAboutIt = it.notifySettings,
            unreadMessageCount = it.unreadCount,
            author = author,
            lastMessage = laseMessage?.toMessage(loadedUsers, messages)
        )
    }
}



// TODO: Перенести расширения toMessage and toUser in common module messanger
fun  SupportWithMessage.toMessage() = ConversationPreview(
    id = conversation.idConversation.toInt(),
    lastMessage = lastMessage.toMessage(),
    notifyAboutIt = conversation.notifyAboutIt,
    unreadMessageCount = conversation.unread,
    title = conversation.title,
    author = conversation.author?.toUser()
)
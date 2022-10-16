package ru.esstu.student.messaging.messenger.supports.datasources

import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import ru.esstu.student.messaging.messenger.datasources.api.response.DataResponse
import ru.esstu.student.messaging.messenger.datasources.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser

fun DataResponse.toSupports(): List<Conversation> {
    return dialogs.filter { dialog -> dialog.type == "SUPPORT" }.mapNotNull {
        val rawConv = conversations.firstOrNull { conv -> conv.id.toString() == it.peerId } ?: return@mapNotNull null
        val author = loadedUsers.firstOrNull { user -> user.id == rawConv.creatorId }?.toUser() ?: return@mapNotNull null
        val laseMessage = messages.firstOrNull { message -> message.id == it.lastMessageId }
        Conversation(
            id = rawConv.id,
            title = rawConv.name,
            notifyAboutIt = it.notifySettings,
            unreadMessageCount = it.unreadCount,
            author = author,
            lastMessage = laseMessage?.toMessage(loadedUsers, messages)
        )
    }
}
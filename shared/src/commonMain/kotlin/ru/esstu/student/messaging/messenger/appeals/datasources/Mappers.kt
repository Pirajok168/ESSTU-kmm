package ru.esstu.student.messaging.messenger.appeals.datasources

import ru.esstu.student.messaging.messenger.appeals.entities.Appeals
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messenger.datasources.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser

fun DataResponse.toAppeals(): List<Appeals> {
    return dialogs.filter { dialog -> dialog.type == "APPEAL" }.mapNotNull {
        val rawConv = conversations.firstOrNull { conv -> conv.id.toString() == it.peerId } ?: return@mapNotNull null
        val author = loadedUsers.firstOrNull { user -> user.id == rawConv.creatorId }?.toUser() ?: return@mapNotNull null
        val laseMessage = messages.firstOrNull { message -> message.id == it.lastMessageId }
        Appeals(
            id = rawConv.id,
            title = rawConv.name,
            notifyAboutIt = it.notifySettings,
            unreadMessageCount = it.unreadCount,
            author = author,
            lastMessage = laseMessage?.toMessage(loadedUsers, messages)
        )
    }
}
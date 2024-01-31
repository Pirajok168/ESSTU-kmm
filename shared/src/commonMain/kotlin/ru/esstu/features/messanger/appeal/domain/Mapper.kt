package ru.esstu.features.messanger.appeal.domain

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.dialogs.domain.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser


fun DataResponse.toAppeals(): List<ConversationPreview> {
    return dialogs.filter { dialog -> dialog.type == "APPEAL" }.mapNotNull {
        val rawConv = conversations.firstOrNull { conv -> conv.id.toString() == it.peerId }
            ?: return@mapNotNull null
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
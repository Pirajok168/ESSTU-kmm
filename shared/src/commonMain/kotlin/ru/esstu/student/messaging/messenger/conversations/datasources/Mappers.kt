package ru.esstu.student.messaging.messenger.conversations.datasources

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations
import ru.esstu.student.messaging.messenger.conversations.datasources.db.entities.ConversationWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.toMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUser

fun TimstampConversations.toTimeStamp() = timestamp

fun Long.toTimeStampEntity(appUserId:String) = TimstampConversations(
    appUserId = appUserId,
    timestamp = this
)

fun DataResponse.toConversations(): List<ConversationPreview> {
    return dialogs.filter { dialog -> dialog.type == "CHAT" }.mapNotNull {
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





fun ConversationWithMessage.toMessage() = ConversationPreview(
    id = conversation.idConversation.toInt(),
    lastMessage = lastMessage.toMessage(),
    notifyAboutIt = conversation.notifyAboutIt,
    unreadMessageCount = conversation.unread,
    title = conversation.title,
    author = conversation.author?.toUser()
)

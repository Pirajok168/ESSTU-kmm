package ru.esstu.student.messaging.messenger.appeals

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messanger.appeals.datasources.db.TimstampAppeals
import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations
import ru.esstu.student.messaging.messenger.appeals.datasources.db.entities.AppealWithMessage

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.toMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.toUser

fun TimstampAppeals.toTimeStamp() = timestamp

fun Long.toTimeStampEntity(appUserId:String) = TimstampAppeals(
    appUserId = appUserId,
    timestamp = this
)

fun AppealWithMessage.toMessage() = ConversationPreview(
    id = appeal.idConversation.toInt(),
    lastMessage = lastMessage.toMessage(),
    notifyAboutIt = appeal.notifyAboutIt,
    unreadMessageCount = appeal.unread,
    title = appeal.title,
    author = appeal.author?.toUser()
)

fun DataResponse.toAppeals(): List<ConversationPreview> {
    return dialogs.filter { dialog -> dialog.type == "APPEAL" }.mapNotNull {
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
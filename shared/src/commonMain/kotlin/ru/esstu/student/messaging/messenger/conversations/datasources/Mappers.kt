package ru.esstu.student.messaging.messenger.conversations.datasources


import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.student.messaging.messenger.datasources.toMessage
import ru.esstu.student.messaging.messenger.datasources.toUser

fun DataResponse.toConversations(): List<Conversation> {
    return dialogs.filter { dialog -> dialog.type == "CHAT" }.mapNotNull {
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

/*
fun ConversationWithMessage.toConversation() = Conversation(
    id = conversation.id,
    title = conversation.title,
    unreadMessageCount = conversation.unreadMessageCount,
    notifyAboutIt = conversation.notifyAboutIt,
    lastMessage = lastMessage?.toMessage(),
    author = conversation.author.toUser()
)

fun Conversation.toConversationWithMessage(appUserId:String, order:Int) = ConversationWithMessage(
    conversation = toConversationEntity(appUserId, order),
    lastMessage = lastMessage?.toMessageWithAttachments()
)


fun Conversation.toConversationEntity(appUserId:String, order:Int): ConversationEntity = ConversationEntity(
    id = id, title = title, unreadMessageCount = unreadMessageCount, notifyAboutIt = notifyAboutIt,
    author = author.toUserEntity(),
    appUserId = appUserId,
    sortOrder = order,
    lastMessageId = lastMessage?.id
)*/

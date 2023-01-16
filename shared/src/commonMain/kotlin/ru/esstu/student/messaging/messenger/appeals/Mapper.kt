package ru.esstu.student.messaging.messenger.appeals

import ru.esstu.student.messaging.messanger.appeals.datasources.db.TimstampAppeals
import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations
import ru.esstu.student.messaging.messenger.appeals.datasources.db.entities.AppealWithMessage

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
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
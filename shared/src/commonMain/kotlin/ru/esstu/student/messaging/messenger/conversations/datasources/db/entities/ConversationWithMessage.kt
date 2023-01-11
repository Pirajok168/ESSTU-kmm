package ru.esstu.student.messaging.messenger.conversations.datasources.db.entities

import ru.esstu.student.messaging.messanger.conversation.datasources.db.ConversationTable
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments

data class ConversationWithMessage(
    val conversation: ConversationTable,
    val lastMessage: LastMessageWithCountAttachments
)

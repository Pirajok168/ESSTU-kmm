package ru.esstu.student.messaging.messenger.conversations.datasources.db

import ru.esstu.student.messaging.messenger.conversations.datasources.db.entities.ConversationWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations.DialogWithMessage

interface ConversationsCacheDao {
    suspend fun clear()
    suspend fun setDialog(appUserId: String, conversation: Conversation)
    suspend fun setLastMessage(message: PreviewLastMessage)
    suspend fun getDialogWithLastMessage(appUserId: String, pageSize: Int, pageOffset: Int): List<ConversationWithMessage>
}
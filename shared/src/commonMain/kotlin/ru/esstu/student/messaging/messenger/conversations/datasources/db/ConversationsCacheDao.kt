package ru.esstu.student.messaging.messenger.conversations.datasources.db

import ru.esstu.student.messaging.messenger.conversations.datasources.db.entities.ConversationWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

interface ConversationsCacheDao {
    suspend fun clear()
    suspend fun setDialog(appUserId: String, conversationPreview: ConversationPreview)
    suspend fun setLastMessage(message: PreviewLastMessage)
    suspend fun getDialogWithLastMessage(appUserId: String, pageSize: Int, pageOffset: Int): List<ConversationWithMessage>
}
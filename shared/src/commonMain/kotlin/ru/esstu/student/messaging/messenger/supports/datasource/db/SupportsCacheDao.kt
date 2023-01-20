package ru.esstu.student.messaging.messenger.supports.datasource.db

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.supports.datasource.db.entities.SupportWithMessage

interface SupportsCacheDao {
    suspend fun clear()
    suspend fun setDialog(appUserId: String, conversationPreview: ConversationPreview)
    suspend fun setLastMessage(message: PreviewLastMessage)

    suspend fun getDialogWithLastMessage(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<SupportWithMessage>

    suspend fun updateDialogLastMessage(
        appUserId: String,
        convId: Int,
        lastMessage: PreviewLastMessage,
        conversationPreview: ConversationPreview? = null,
        isCreateNewSupport: Boolean = false
    )
}
package ru.esstu.student.messaging.messenger.appeals.datasources.db

import ru.esstu.student.messaging.messenger.appeals.datasources.db.entities.AppealWithMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

interface AppealsCacheDao {

    suspend fun clear()
    suspend fun setDialog(appUserId: String, conversationPreview: ConversationPreview)
    suspend fun setLastMessage(message: PreviewLastMessage)
    suspend fun getDialogWithLastMessage(
        appUserId: String,
        pageSize: Int,
        pageOffset: Int
    ): List<AppealWithMessage>

    suspend fun updateDialogLastMessage(
        appUserId: String,
        convId: Int,
        lastMessage: PreviewLastMessage,
        appeal: ConversationPreview? = null,
        isCreate: Boolean = false
    )
}
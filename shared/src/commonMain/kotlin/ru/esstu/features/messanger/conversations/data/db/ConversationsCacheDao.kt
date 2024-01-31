package ru.esstu.features.messanger.conversations.data.db

import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface ConversationsCacheDao {

    suspend fun setConversation(previewDialog: ConversationPreview)

    suspend fun getConversations(pageSize: Int, pageOffset: Int): List<ConversationPreview>

    suspend fun clearDialogs()
}
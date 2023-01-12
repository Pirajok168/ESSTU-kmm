package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface IConversationsDbRepository {
    suspend fun getConversations(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun clear()

    suspend fun setConversations(previewDialogs: List<ConversationPreview>)

    suspend fun deleteDialog(id: String)
}
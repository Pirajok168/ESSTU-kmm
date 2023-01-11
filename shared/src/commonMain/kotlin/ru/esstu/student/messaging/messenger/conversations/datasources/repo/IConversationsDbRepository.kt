package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.student.messaging.messenger.conversations.entities.Conversation

interface IConversationsDbRepository {
    suspend fun getConversations(limit: Int, offset: Int): List<Conversation>

    suspend fun clear()

    suspend fun setConversations(previewDialogs: List<Conversation>)

    suspend fun deleteDialog(id: String)
}
package ru.esstu.student.messaging.messenger.supports.datasource.repo

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface ISupportsDbRepository {
    suspend fun getSupports(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun clear()

    suspend fun setSupports(previewDialogs: List<ConversationPreview>)

    suspend fun deleteDialog(id: String)
}
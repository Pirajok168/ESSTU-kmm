package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface IAppealsDbRepository {
    suspend fun getAppeals(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun clear()

    suspend fun setAppeals(previewDialogs: List<ConversationPreview>)

    suspend fun deleteDialog(id: String)
}
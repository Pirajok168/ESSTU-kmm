package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface IConversationsApiRepository {
    suspend fun getConversations(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
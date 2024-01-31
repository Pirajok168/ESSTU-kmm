package ru.esstu.features.messanger.conversations.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface IConversationsApiRepository {
    suspend fun getConversations(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
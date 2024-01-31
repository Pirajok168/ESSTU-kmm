package ru.esstu.features.messanger.conversations.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.data.api.ConversationsApi
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.conversations.domain.toConversations

class ConversationsApiRepositoryImpl(
    private val api: ConversationsApi
) : IConversationsApiRepository {
    override suspend fun getConversations(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> =
        api.getConversations(offset, limit).transform { it.toConversations() }
}
package ru.esstu.features.messanger.conversations.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface ConversationsInteractor {
    suspend fun getLocalConversation(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun getConversation(limit: Int, offset: Int): Response<List<ConversationPreview>>

    suspend fun clearConversation()
}
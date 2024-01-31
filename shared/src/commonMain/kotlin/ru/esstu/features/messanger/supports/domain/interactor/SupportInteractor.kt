package ru.esstu.features.messanger.supports.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface SupportInteractor {
    suspend fun getLocalSupports(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun getSupports(limit: Int, offset: Int): Response<List<ConversationPreview>>

    suspend fun clearSupports()
}
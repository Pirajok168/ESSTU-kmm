package ru.esstu.features.messanger.supports.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface ISupportsApiRepository {
    suspend fun getSupports(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
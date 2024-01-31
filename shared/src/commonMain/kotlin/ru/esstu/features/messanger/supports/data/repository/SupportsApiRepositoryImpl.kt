package ru.esstu.features.messanger.supports.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.supports.data.api.SupportsApi
import ru.esstu.features.messanger.supports.domain.toSupports

class SupportsApiRepositoryImpl(
    private val api: SupportsApi
) : ISupportsApiRepository {
    override suspend fun getSupports(limit: Int, offset: Int): Response<List<ConversationPreview>> =
        api.getSupports(offset, limit).transform { it.toSupports() }
}
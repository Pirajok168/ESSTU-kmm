package ru.esstu.features.messanger.appeal.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.appeal.data.api.AppealsApi
import ru.esstu.features.messanger.appeal.domain.toAppeals
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

class AppealsApiRepositoryImpl(
    private val api: AppealsApi
) : IAppealsApiRepository {
    override suspend fun getAppeals(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> =
        api.getAppeals(offset, limit).transform { it.toAppeals() }
}
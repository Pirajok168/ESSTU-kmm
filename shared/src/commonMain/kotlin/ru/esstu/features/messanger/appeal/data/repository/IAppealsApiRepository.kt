package ru.esstu.features.messanger.appeal.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface IAppealsApiRepository {
    suspend fun getAppeals(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
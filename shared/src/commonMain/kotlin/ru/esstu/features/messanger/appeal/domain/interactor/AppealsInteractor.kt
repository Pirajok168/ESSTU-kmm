package ru.esstu.features.messanger.appeal.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface AppealsInteractor {
    suspend fun getLocalAppeals(limit: Int, offset: Int): List<ConversationPreview>

    suspend fun getAppeals(limit: Int, offset: Int): Response<List<ConversationPreview>>

    suspend fun clearAppeals()
}
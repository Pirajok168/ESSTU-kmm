package ru.esstu.features.messanger.appeal.data.repository

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

interface IAppealUpdatesRepository {
    suspend fun installObserving(): Flow<Response<List<ConversationPreview>>>
}
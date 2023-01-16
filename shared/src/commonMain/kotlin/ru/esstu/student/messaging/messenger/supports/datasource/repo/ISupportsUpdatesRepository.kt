package ru.esstu.student.messaging.messenger.supports.datasource.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface ISupportsUpdatesRepository {
    suspend fun installObserving(): Flow<Response<List<ConversationPreview>>>
}
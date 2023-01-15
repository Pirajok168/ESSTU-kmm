package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface IConversationUpdatesRepository {

    suspend fun installObserving(): Flow<Response<List<ConversationPreview>>>


}
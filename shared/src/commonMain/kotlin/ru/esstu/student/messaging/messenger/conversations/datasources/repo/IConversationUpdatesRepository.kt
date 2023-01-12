package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation

interface IConversationUpdatesRepository {
    val updatesFlow: Flow<Response<List<Conversation>>>
}
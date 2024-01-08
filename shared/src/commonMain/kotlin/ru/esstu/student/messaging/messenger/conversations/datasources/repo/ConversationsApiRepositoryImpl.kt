package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationsApi
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

class ConversationsApiRepositoryImpl(
    private val api: ConversationsApi
): IConversationsApiRepository {
    override suspend fun getConversations(limit: Int, offset: Int): Response<List<ConversationPreview>>  =
        api.getConversations(offset, limit).transform { it.toConversations() }
}
package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationsApi
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

class ConversationsApiRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: ConversationsApi
): IConversationsApiRepository {
    override suspend fun getConversations(limit: Int, offset: Int): Response<List<ConversationPreview>>  =
        auth.provideToken { type, token ->
            api.getConversations("$token", offset, limit).toConversations()
        }
}
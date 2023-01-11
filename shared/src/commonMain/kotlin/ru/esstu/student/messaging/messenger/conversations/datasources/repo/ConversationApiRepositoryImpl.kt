package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApi
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation

class ConversationApiRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: ConversationApi
): IConversationApiRepository {
    override suspend fun getConversations(limit: Int, offset: Int): Response<List<Conversation>>  =
        auth.provideToken { type, token ->
            api.getConversations("$token", offset, limit).toConversations()
        }
}
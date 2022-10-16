package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.student.messaging.messenger.conversations.datasources.api.ConversationApi
import ru.esstu.student.messaging.messenger.conversations.datasources.toConversations



class ConversationApiRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val api: ConversationApi
) : IConversationApiRepository {
    override suspend fun getConversations(limit: Int, offset: Int) =
        auth.provideToken { type, token ->
            api.getConversations("$token", offset, limit).toConversations()
        }
}
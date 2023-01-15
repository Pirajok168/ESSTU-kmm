package ru.esstu.student.messaging.messenger.supports.datasource.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.supports.datasource.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.toSupports

class SupportsApiRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: SupportsApi
): ISupportsApiRepository {
    override suspend fun getSupports(limit: Int, offset: Int): Response<List<ConversationPreview>> =
        auth.provideToken { type, token ->
            api.getSupports("$token", offset, limit).toSupports()
        }
}
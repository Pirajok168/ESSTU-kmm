package ru.esstu.student.messaging.messenger.supports.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.datasources.toSupports
import ru.esstu.student.messaging.messenger.supports.entities.Supports


class SupportsApiRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val api: SupportsApi
) : ISupportsApiRepository {
     override suspend fun getSupports(limit: Int, offset: Int): Response<List<Supports>> =
        auth.provideToken {  token ->
            api.getSupports(token.access, offset, limit).toSupports()
        }
}
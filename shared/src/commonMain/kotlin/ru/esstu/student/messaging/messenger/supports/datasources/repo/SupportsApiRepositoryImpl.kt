package ru.esstu.student.messaging.messenger.supports.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.student.messaging.messenger.supports.datasources.api.SupportsApi
import ru.esstu.student.messaging.messenger.supports.datasources.toSupports



class SupportsApiRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val api: SupportsApi
) : ISupportsApiRepository {
     override suspend fun getSupports(limit: Int, offset: Int) =
        auth.provideToken { type, token ->
            api.getSupports("$token", offset, limit).toSupports()
        }
}
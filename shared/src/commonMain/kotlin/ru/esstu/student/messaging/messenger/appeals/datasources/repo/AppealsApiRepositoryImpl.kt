package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApi
import ru.esstu.student.messaging.messenger.appeals.datasources.toAppeals



class AppealsApiRepositoryImpl constructor(
    private val auth: IAuthRepository,
    private val api: AppealsApi
) : IAppealsApiRepository {
     override suspend fun getSupports(limit: Int, offset: Int) =
        auth.provideToken { type, token ->
            api.getAppeals("$token", offset, limit).toAppeals()
        }
}
package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.appeals.datasources.api.AppealsApi
import ru.esstu.student.messaging.messenger.appeals.toAppeals
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

class AppealsApiRepositoryImpl(
    private val api: AppealsApi
): IAppealsApiRepository {
    override suspend fun getAppeals(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> = api.getAppeals(offset, limit).transform { it.toAppeals() }
}
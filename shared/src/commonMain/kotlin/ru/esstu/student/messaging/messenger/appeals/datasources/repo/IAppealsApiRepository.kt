package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface IAppealsApiRepository {
    suspend fun getAppeals(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
package ru.esstu.student.messaging.messenger.supports.datasource.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

interface ISupportsApiRepository {
    suspend fun getSupports(limit: Int, offset: Int): Response<List<ConversationPreview>>
}
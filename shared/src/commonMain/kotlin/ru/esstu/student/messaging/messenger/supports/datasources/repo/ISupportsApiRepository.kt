package ru.esstu.student.messaging.messenger.supports.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation

interface ISupportsApiRepository {
    suspend fun getSupports(limit: Int, offset: Int): Response<List<Conversation>>
}
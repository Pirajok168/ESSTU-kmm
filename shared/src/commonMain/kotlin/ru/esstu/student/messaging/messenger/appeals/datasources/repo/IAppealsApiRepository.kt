package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation

interface IAppealsApiRepository {
    suspend fun getSupports(limit: Int, offset: Int): Response<List<Conversation>>
}
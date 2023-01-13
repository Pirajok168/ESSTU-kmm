package ru.esstu.student.messaging.group_chat.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.Message

interface IGroupChatUpdateRepository {
    fun collectUpdates(convId: Int, lastMessageId: Long): Flow<Response<List<Message>>>
}
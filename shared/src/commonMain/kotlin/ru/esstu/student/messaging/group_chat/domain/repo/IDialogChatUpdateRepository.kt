package ru.esstu.student.messaging.group_chat.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.Message

interface IGroupChatUpdateRepository {
    fun collectUpdates(convId: Int, lastMessageId: Long): Flow<Response<List<Message>>>
}
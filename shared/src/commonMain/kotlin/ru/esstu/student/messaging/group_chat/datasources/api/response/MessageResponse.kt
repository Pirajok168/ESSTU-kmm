package ru.esstu.student.messaging.group_chat.datasources.api.response

import kotlinx.serialization.Serializable
import ru.esstu.student.messaging.messenger.datasources.api.response.Conversation
import ru.esstu.student.messaging.messenger.datasources.api.response.Message
import ru.esstu.student.messaging.messenger.datasources.api.response.User

@Serializable
data class MessageResponse(
    val chats: List<Conversation>,
    val messages: List<Message>,
    val users: List<User>
)

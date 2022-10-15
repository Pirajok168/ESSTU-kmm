package ru.esstu.student.messaging.group_chat.datasources.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.student.messaging.messenger.datasources.api.response.Conversation
import ru.esstu.student.messaging.messenger.datasources.api.response.User
@Serializable
data class ConversationResponse(
    @SerialName("chat")
    val conversation:Conversation,
    val notifySettings: Boolean,
    val participants: List<ParticipantResponse>,
    val users: List<User>
)

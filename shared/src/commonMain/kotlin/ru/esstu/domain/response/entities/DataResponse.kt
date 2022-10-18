package ru.esstu.domain.response.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DataResponse(
    @SerialName("chats")
    val conversations: List<ConversationResponse>,
    @SerialName("dialogs")
    val dialogs: List<DialogResponse>,
    val messages: List<MessageResponse>,
    @SerialName("users")
    val loadedUsers: List<UserResponse>
)
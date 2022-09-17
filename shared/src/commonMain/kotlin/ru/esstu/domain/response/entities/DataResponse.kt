package ru.esstu.domain.response.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DataResponse(
    @SerialName("chats")
    val conversations: List<Conversation>,
    @SerialName("dialogs")
    val dialogs: List<Dialog>,
    val messages: List<Message>,
    @SerialName("users")
    val loadedUsers: List<UserResponse>
)
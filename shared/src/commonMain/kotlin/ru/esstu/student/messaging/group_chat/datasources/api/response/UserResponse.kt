package ru.esstu.student.messaging.group_chat.datasources.api.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.student.messaging.messenger.datasources.api.response.User

@Serializable
data class UserResponse(
    val notifySettings: Boolean,
    @SerialName("user")
    val user: User
)

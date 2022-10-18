package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.ConversationPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.DialogPreview

@Serializable
data class DataResponse(
    @SerialName("chats")
    val conversations: List<ConversationPreview>,
    @SerialName("dialogs")
    val dialogs: List<DialogPreview>,
    val messages: List<ChatMessage>,
    @SerialName("users")
    val loadedUsers: List<UserPreview>
)
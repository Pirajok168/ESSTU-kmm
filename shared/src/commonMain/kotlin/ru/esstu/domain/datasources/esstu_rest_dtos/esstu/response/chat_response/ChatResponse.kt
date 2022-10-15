package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response


import kotlinx.serialization.SerialName
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response.inner_classes.ConversationInfo


data class ChatResponse(
    @SerialName("chats")
    val ConversationInfos: List<ConversationInfo>,
    val messages: List<ChatMessage>,
    val users: List<UserPreview>
)
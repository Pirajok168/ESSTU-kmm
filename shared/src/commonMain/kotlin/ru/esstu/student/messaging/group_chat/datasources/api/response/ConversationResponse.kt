package ru.esstu.student.messaging.group_chat.datasources.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.ConversationPreviewResponse


@Serializable
data class ConversationResponse(
    @SerialName("chat")
    val conversation: ConversationPreviewResponse,
    val notifySettings: Boolean,
    val participants: List<ParticipantResponse>,
    val users: List<UserPreview>
)

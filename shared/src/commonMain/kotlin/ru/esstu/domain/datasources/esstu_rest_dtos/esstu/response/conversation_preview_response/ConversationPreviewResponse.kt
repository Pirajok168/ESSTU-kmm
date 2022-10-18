package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response.inner_classes.ConversationInfo

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes.ParticipantGroup

//этот запрос необязательный, т.к. всю необходимую информацию можно получить через чат реквест. Но он используется в лк и хз зачем
@Serializable
data class ConversationPreviewResponse(
    @SerialName("chat")
    val conversation: ConversationInfo,
    val notifySettings: Boolean,
    val participants: List<ParticipantGroup>,
    val users: List<UserPreview>
)
package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response


import kotlinx.serialization.SerialName
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response.inner_classes.ConversationInfo
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes.ConversationUserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.conversation_preview_response.inner_classes.ParticipantGroup

//этот запрос необязательный, т.к. всю необходимую информацию можно получить через чат реквест. Но он используется в лк и хз зачем
data class ConversationPreviewResponse(
    @SerialName("chat")
    val currentConversationInfo: ConversationInfo,
    val notifySettings: Boolean,
    @SerialName("participants")
    val participantGroups: List<ParticipantGroup>,
    val users: List<ConversationUserPreview>
)
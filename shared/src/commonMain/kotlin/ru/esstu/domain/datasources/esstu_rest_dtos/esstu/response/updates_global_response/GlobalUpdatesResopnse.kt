package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.updates_global_response

import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response.inner_classes.ConversationInfo
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.DialogPreview


data class GlobalUpdatesResopnse(
    val chats: List<ConversationInfo>,
    val dialogs: List<DialogPreview>,
    val messages: List<ChatMessage>,
    val users: List<UserPreview>
)
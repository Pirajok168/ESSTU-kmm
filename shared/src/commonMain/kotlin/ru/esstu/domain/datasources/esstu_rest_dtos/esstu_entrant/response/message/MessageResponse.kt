package ru.esstu.domain.datasources.esstu_rest_dtos.esstu_entrant.response.message

import kotlinx.serialization.Serializable
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes.FileAttachmentResponse


@Serializable
data class MessageResponse(
    val chats: List<ChatsPreview>,
    val messages: List<MessagePreview>,
    val users: List<UserPreview>
)

@Serializable
data class MessagePreview(
    val attachments: List<FileAttachmentResponse>,
    val context: String?,
    val date: Long,
    val flags: Int,
    val from: String,
    val id: Long,
    val message: String?,
    val peerId: String,
    val replyToMsgId: Long?,
    val type: Int,
    val views: Int
)


@Serializable
data class ChatsPreview(
    val id: Int,
    val type: String,
    val name: String,
    val description: String?,
    val context: String?,
    val participantsCount: Int,
    val date: Long,
    val creatorId:String,
    val flags: Int
)
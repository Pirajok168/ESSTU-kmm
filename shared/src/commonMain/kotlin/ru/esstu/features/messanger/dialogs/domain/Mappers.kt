package ru.esstu.features.messanger.dialogs.domain


import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.ChatMessage
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.api_common.UserPreview
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.DataResponse
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.ReplyMessage
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.datasources.toUser


//</editor-fold>

//<editor-fold desc="DialogsApiRepositoryImpl">
fun DataResponse.toDialogs(): List<PreviewDialog> {
    return dialogs.filter { it.type == "DIALOGUE" }.mapNotNull { dialog ->
        val opponent = loadedUsers.firstOrNull { user -> user.id == dialog.peerId }?.toUser()
        PreviewDialog(
            id = dialog.peerId,
            unreadMessageCount = dialog.unreadCount,
            opponent = opponent ?: return@mapNotNull null,
            lastMessage = messages.firstOrNull { message -> message.id == dialog.lastMessageId }
                ?.toMessage(loadedUsers, messages),
            notifyAboutIt = dialog.notifySettings
        )
    }
}

fun ChatMessage.toMessage(
    loadedUsers: List<UserPreview>,
    loadedMessages: List<ChatMessage>
): PreviewLastMessage? {
    return PreviewLastMessage(
        id = id,
        date = date,
        message = message.orEmpty(),
        attachments = attachments.size,
        from = loadedUsers.firstOrNull { user -> user.id == this.from }?.toUser() ?: return null,
        replyMessage = if (replyToMsgId != null) loadedMessages.firstOrNull { it.id == replyToMsgId }
            ?.let { replyMessage ->
                ReplyMessage(
                    id = replyMessage.id,
                    date = date,
                    message = replyMessage.message.orEmpty(),
                    attachmentsCount = replyMessage.attachments.size,
                    from = loadedUsers.firstOrNull { user -> user.id == replyMessage.from }
                        ?.toUser() ?: return@let null
                )

            } else null,
        status = if (views > 1) DeliveryStatus.READ else DeliveryStatus.DELIVERED
    )
}
//</editor-fold>


//<editor-fold desc="CacheDatabase">
fun Sender.toUserEntity() = UserEntity(
    summary = summary,
    photo = photo,
    lastName = lastName,
    firstName = firstName,
    patronymic = patronymic,
    id = id
)

//</editor-fold>


//<editor-fold desc="DialogsDbRepositoryImpl">

fun UserEntity.toUser() = Sender(
    id = id,
    summary = summary, photo = photo,
    lastName = lastName, firstName = firstName, patronymic = patronymic
)

//</editor-fold>

fun Message.toPreviewLastMessage() = PreviewLastMessage(
    id = id,
    from = from,
    date = date,
    message = message,
    replyMessage = replyMessage,
    status = status,
    attachments.size
)





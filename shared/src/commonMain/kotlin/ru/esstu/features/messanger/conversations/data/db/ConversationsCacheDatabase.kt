package ru.esstu.features.messanger.conversations.data.db

import kotlinx.datetime.Clock
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.dialogs.domain.toUser
import ru.esstu.features.messanger.dialogs.domain.toUserEntity
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage

class ConversationsCacheDatabase(
    val dbQueries: ConversationQueries
) : ConversationsCacheDao {
    override suspend fun setConversation(previewDialog: ConversationPreview) {
        previewDialog.run {
            previewDialog.lastMessage?.let {
                dbQueries.setLastMessage(it.id, it.from.toUserEntity(), it.date, it.message)
            }

            dbQueries.setConversation(
                id.toLong(),
                title,
                lastMessage?.id,
                author?.toUserEntity(),
                unreadMessageCount,
                notifyAboutIt
            )
        }
    }

    override suspend fun getConversations(
        pageSize: Int,
        pageOffset: Int
    ): List<ConversationPreview> =
        dbQueries.getConversation(pageSize.toLong(), pageOffset.toLong()).executeAsList()
            .map { remote ->

                ConversationPreview(
                    id = remote.idConversation.toInt(),
                    title = remote.title,
                    author = remote.author?.toUser(),
                    lastMessage = remote.messageId?.let {
                        PreviewLastMessage(
                            id = it,
                            from = remote.author_?.toUser()!!,
                            date = remote.date ?: Clock.System.now().toEpochMilliseconds(),
                            message = remote.message.orEmpty(),
                            replyMessage = null,
                            status = DeliveryStatus.DELIVERED,
                            attachments = 0
                        )
                    },
                    notifyAboutIt = remote.notifyAboutIt,
                    unreadMessageCount = remote.unread
                )
            }

    override suspend fun clearDialogs() = dbQueries.clear()


}
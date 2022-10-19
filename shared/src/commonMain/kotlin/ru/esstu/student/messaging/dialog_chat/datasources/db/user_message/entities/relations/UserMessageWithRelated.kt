package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.relations

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_message.entities.UserMessageEntity

data class UserMessageWithRelated (
    val message: UserMessageEntity,
    val reply: MessageWithRelated?,
    val attachments: List<UserCachedFileEntity>
)
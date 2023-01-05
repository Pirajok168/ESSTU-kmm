package ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities

import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity

data class LastMessageWithCountAttachments(
    val message: MessageEntity,
    val attachments: Int
) {
}
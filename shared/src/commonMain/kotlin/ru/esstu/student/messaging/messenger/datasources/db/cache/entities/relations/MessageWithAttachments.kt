package ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations


import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.AttachmentEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.MessageEntity

data class MessageWithAttachments(

    val message: MessageEntity,

    val attachments:List<AttachmentEntity>
)

package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.relations


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity

data class ErredMessageWithRelated(
    val message: ErredMessageEntity,
    val reply: MessageWithRelated?,
    val attachments: List<ErredCachedFileEntity>
)
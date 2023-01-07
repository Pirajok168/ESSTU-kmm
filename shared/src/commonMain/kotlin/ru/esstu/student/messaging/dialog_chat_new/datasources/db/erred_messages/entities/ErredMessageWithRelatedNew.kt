package ru.esstu.student.messaging.dialog_chat_new.datasources.db.erred_messages.entities


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations.MessageWithRelated
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredCachedFileEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.entities.ErredMessageEntity
import ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredMessageTableNew

data class ErredMessageWithRelatedNew(
    val message: ErredMessageTableNew,
    val reply: MessageWithRelatedNew?,
    val attachments: List<ErredCachedFileTableNew>
)
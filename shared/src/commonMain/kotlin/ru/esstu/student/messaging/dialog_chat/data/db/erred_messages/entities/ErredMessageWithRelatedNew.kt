package ru.esstu.student.messaging.dialog_chat.data.db.erred_messages.entities


import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredMessageTableNew

data class ErredMessageWithRelatedNew(
    val message: ErredMessageTableNew,
    val reply: MessageWithRelatedNew?,
    val attachments: List<ErredCachedFileTableNew>
)
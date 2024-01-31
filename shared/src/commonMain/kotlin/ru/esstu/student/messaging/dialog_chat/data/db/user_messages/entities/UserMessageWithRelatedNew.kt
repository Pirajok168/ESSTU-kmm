package ru.esstu.student.messaging.dialog_chat.data.db.user_messages.entities


import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable

data class UserMessageWithRelatedNew(
    val message: UserMessageEntityTable,
    val reply: MessageWithRelatedNew?,
    val attachments: List<UserCachedFileTable>
)
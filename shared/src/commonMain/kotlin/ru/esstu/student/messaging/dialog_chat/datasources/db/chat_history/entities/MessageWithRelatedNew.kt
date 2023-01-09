package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities



import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAttachmentTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew

data class MessageWithRelatedNew(

    val message: DialogChatMessageTableNew,

    val attachments: List<DialogChatAttachmentTableNew>,

    val reply: DialogChatReplyMessageTableNew?,
)



data class MessageWithRelatedEntityNew(
    val message: DialogChatMessageTableNew,

    val attachments: DialogChatAttachmentTableNew?,

    val reply: DialogChatReplyMessageTableNew?,
)
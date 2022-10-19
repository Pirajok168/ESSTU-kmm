package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.relations


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAttachmentEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatReplyMessageEntity

data class MessageWithRelated(

    val message: DialogChatMessageEntity,


    val attachments: List<DialogChatAttachmentEntity>,


    val reply: DialogChatReplyMessageEntity?,
)
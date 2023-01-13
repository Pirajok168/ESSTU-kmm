package ru.esstu.student.messaging.group_chat.datasources.db.erred_messages.entities

import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredCachedFile
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredMessage

data class GroupChatErredMessageWithRelated(
    val message: GroupChatErredMessage,
    val reply: MessageGroupChatWithRelated?,
    val attachments: List<GroupChatErredCachedFile>
)
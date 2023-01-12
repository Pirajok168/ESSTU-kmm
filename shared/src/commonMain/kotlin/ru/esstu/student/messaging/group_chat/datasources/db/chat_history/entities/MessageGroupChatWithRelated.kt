package ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities

import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatAttachment
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage


data class MessageGroupChatWithRelated(

    val message: GroupChatMessage,


    val attachments: List<GroupChatAttachment>,


    val reply: GroupChatReplyMessage?,
)


data class MessageGroupChatWithRelatedEntity(

    val message: GroupChatMessage,


    val attachments: GroupChatAttachment?,


    val reply: GroupChatReplyMessage?,
)
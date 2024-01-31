package ru.esstu.student.messaging.group_chat.data.db.user_messages.entities


import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserCachedFileEntity
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserMessage

data class GroupChatUserMessageWithRelated(
    val message: GroupChatUserMessage,
    val reply: MessageGroupChatWithRelated?,
    val attachments: List<GroupChatUserCachedFileEntity>
)
package ru.esstu.student.messaging.group_chat.datasources.db.user_messages.entities


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.MessageWithRelatedNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserMessageEntityTable
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.MessageGroupChatWithRelated
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserCachedFileEntity
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserMessage

data class GroupChatUserMessageWithRelated (
    val message: GroupChatUserMessage,
    val reply: MessageGroupChatWithRelated?,
    val attachments: List<GroupChatUserCachedFileEntity>
)
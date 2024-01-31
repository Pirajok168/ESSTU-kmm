package ru.esstu.student.messaging.group_chat.data.db.header.entities

import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatParticipant

data class ConversationWithParticipants(
    val conversation: GroupChatConversation,
    val participants: List<GroupChatParticipant>
)

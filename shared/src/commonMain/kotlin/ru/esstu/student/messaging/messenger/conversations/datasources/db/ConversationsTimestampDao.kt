package ru.esstu.student.messaging.messenger.conversations.datasources.db

import ru.esstu.student.messaging.messanger.conversation.datasources.db.TimstampConversations
import ru.esstu.student.messaging.messanger.dialogs.datasources.db.TimstampDialogs

interface ConversationsTimestampDao {
    suspend fun getTimestamp(appUserId: String): TimstampConversations?


    suspend fun setTimestamp(timestamp: TimstampConversations)
}
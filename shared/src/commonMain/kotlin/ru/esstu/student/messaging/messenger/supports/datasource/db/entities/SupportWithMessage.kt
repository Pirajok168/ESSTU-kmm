package ru.esstu.student.messaging.messenger.supports.datasource.db.entities

import ru.esstu.student.messaging.messanger.conversation.datasources.db.ConversationTable
import ru.esstu.student.messaging.messanger.supports.datasources.db.SuppotTable
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments

data class SupportWithMessage(
    val conversation: SuppotTable,
    val lastMessage: LastMessageWithCountAttachments
)

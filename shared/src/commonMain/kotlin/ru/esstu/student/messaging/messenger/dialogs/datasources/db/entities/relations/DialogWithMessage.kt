package ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.relations


import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.relations.MessageWithAttachments
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.DialogEntity
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.entities.LastMessageWithCountAttachments


data class DialogWithMessage(
    val dialog: DialogEntity,
    val lastMessage: LastMessageWithCountAttachments?
)
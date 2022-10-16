package ru.esstu.student.messaging.messenger.datasources.api.response

import kotlinx.serialization.Serializable


@Serializable
data class Dialog(
    val lastMessageId: Long,
    val notifySettings: Boolean,
    val peerId: String,
    val type: String,
    val unreadCount: Int
)
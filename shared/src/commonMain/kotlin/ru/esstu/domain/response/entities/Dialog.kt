package ru.esstu.domain.response.entities

import kotlinx.serialization.Serializable

@Serializable
data class Dialog(
    val lastMessageId: Long,
    val notifySettings: Boolean,
    val peerId: String,
    val type: String,
    val unreadCount: Int
)
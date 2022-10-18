package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes

import kotlinx.serialization.Serializable

@Serializable
data class DialogPreview(
    val lastMessageId: Long,
    val notifySettings: Boolean,
    val peerId: String,
    val type: String,
    val unreadCount: Int
)
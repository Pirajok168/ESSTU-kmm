package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes


data class DialogPreview(
    val lastMessageId: Int,
    val notifySettings: Boolean,
    val peerId: String,
    val type: String,
    val unreadCount: Int
)
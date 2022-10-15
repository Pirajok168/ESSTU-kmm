package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.data_response.inner_classes


data class ConversationPreview(
    val context: Any,
    val date: Long,
    val description: Any,
    val flags: Int,
    val id: Int,
    val listViewrs: Any,
    val name: String,
    val participantsCount: Int,
    val status: String,
    val type: String
)
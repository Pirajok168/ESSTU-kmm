package ru.esstu.domain.datasources.esstu_rest_dtos.esstu.response.chat_response.inner_classes

data class ConversationInfo(
    val confirm: String,
    val confirmCount: Int,
    val context: Any,
    val contextLevel1: Any,
    val contextLevel2: Any,
    val contextLevel3: Any,
    val date: Long,
    val description: Any,
    val flags: Int,
    val id: Int,
    val listViewrs: Any,
    val name: String,
    val participantsCount: Int,
    val status: Any,
    val type: String,
    val viewConfirm: String
)
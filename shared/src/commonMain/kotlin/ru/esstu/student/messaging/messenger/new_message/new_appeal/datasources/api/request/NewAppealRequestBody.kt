package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request

@kotlinx.serialization.Serializable
data class NewAppealRequestBody(
    val message: String? = null,
    val type: String,
    val users: List<String>
)

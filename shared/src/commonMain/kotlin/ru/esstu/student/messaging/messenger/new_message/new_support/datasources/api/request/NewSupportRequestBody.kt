package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.request

@kotlinx.serialization.Serializable
data class NewSupportRequestBody(
    val message: String? = null,
    val type: String,
    val users: List<String>
)

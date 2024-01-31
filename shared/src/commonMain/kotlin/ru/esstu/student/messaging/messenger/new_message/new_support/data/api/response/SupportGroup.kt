package ru.esstu.student.messaging.messenger.new_message.new_support.data.api.response


@kotlinx.serialization.Serializable
data class SupportGroup(
    val id: String,
    val name: String,
    val shortName: String?,
    val usersCount: Int,
    val hasSubgroups: Boolean,
)

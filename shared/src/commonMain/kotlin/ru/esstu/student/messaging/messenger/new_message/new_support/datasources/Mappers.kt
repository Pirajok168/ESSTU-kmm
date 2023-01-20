package ru.esstu.student.messaging.messenger.new_message.new_support.datasources

import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.response.SupportGroup
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

fun SupportGroup.toSupportGroup() = SupportTheme(
    id = id,
    name = name,
    abbreviation = shortName
)
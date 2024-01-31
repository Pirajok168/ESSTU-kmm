package ru.esstu.student.messaging.messenger.new_message.new_support.domain

import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.response.SupportGroup
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

fun SupportGroup.toSupportGroup() = SupportTheme(
    id = id,
    name = name,
    abbreviation = shortName
)
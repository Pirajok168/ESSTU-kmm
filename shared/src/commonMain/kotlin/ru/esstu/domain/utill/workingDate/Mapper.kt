package ru.esstu.domain.utill.workingDate


import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime


fun Long.toFormatString(regexDate: String): String {
    val localeDate = Instant.fromEpochMilliseconds(this).toLocalDateTime()
        .toInstant()
    return localeDate.format(regexDate)
}



fun LocalDateTime.format(pattern: String): String =
    toInstant().format(pattern)
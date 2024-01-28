package ru.esstu.domain.utill.workingDate

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


internal val currentTimezone = TimeZone.currentSystemDefault()
fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(currentTimezone)

fun LocalDateTime.toInstant(): Instant = toInstant(currentTimezone)

fun Instant.format(pattern: String): String = format(pattern, toEpochMilliseconds())
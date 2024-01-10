package ru.esstu.domain.utill.workingDate

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun format(pattern: String, millis: Long): String {
    val dateFormat: (String) -> DateFormat = { SimpleDateFormat(it, Locale.forLanguageTag("ru")) }
    return dateFormat(pattern).format(Date(millis))
}
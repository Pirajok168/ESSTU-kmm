package ru.esstu.domain.utill.workingDate

import android.content.Context.MODE_PRIVATE
import ru.esstu.ContextApplication
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun format(pattern: String, millis: Long): String {
    val locale = ContextApplication.getContextApplication().context.getSharedPreferences("SELECTED_LOCALE", MODE_PRIVATE)
        .getString("SELECTED_LOCALE_TAG", "ru") ?: "ru"
    val dateFormat: (String) -> DateFormat = { SimpleDateFormat(it, Locale.forLanguageTag(locale)) }
    return dateFormat(pattern).format(Date(millis))
}
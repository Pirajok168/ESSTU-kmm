package ru.esstu.domain.utill.workingDate

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

internal const val defaultLocaleLanguageTag = "ru"
val defaultLocale = NSLocale(localeIdentifier = defaultLocaleLanguageTag)
const val MILLIS_IN_SECOND: Long = 1000L

actual fun format(pattern: String, millis: Long): String = with(NSDateFormatter()) {
    locale = defaultLocale
    setDateFormat(pattern)
    stringFromDate(NSDate.dateWithTimeIntervalSince1970((millis / MILLIS_IN_SECOND).toDouble()))
}
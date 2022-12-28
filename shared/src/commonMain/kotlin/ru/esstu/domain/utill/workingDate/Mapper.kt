package ru.esstu.domain.utill.workingDate

import com.soywiz.klock.*
import com.soywiz.klock.locale.russian


fun Long.toFormatString(regexDate: String): String{
    val locale = KlockLocale.russian
    return DateTime(this).local.format(format = regexDate, locale = locale)
}
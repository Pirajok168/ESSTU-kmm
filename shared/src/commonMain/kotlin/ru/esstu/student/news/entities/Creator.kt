package ru.esstu.student.news.entities

import kotlinx.serialization.Serializable

@Serializable
data class Creator(
    val id:String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val summary: String,
    val photo: String?,
) {
    val initials: String get() = "${lastName.take(1)}${firstName.take(1)}${patronymic.take(1)}"
    val fio: String get() = "$lastName $firstName $patronymic".trim()
    val shortFio: String
        get() = "$lastName ${firstName.take(1)}. ${
            if (patronymic.isNotBlank())
                "${patronymic.take(1)}."
            else
                ""
        }"
}

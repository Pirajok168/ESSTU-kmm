package ru.esstu.student.profile.student.main_profile.domain.model

data class StudentProfile(
    val lastName: String, // фамилия
    val photo: String?,
    val firstName: String, // имя
    val patronymic: String, // отчество
    val group: String, // группа
    val course: String, // курс
    val cathedra: String, // кафедра
    val faculty: String, // факультет
    val standard: String, // стандарт обучения
    val directionCode: String, // код направления
    val directionName: String, // наименование направления
    val profile: String, // профиль
    val studyForm: String, // форма обучения
    val studyLevel: String, // уровень образования (бакалавриат, магистратура и др)
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
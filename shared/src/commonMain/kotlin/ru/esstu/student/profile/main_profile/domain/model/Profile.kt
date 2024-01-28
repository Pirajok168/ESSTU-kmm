package ru.esstu.student.profile.main_profile.domain.model

sealed interface Profile {
    val lastName: String
    val firstName: String
    val patronymic: String

    fun fio() =
        "$lastName $firstName $patronymic".trim()

    fun initials() =
        "${lastName.take(1)}${firstName.take(1)}${patronymic.take(1)}"

    data class StudentProfile(
        override val lastName: String, // фамилия
        override val firstName: String, // имя
        override val patronymic: String, // отчество
        val photo: String?,
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
    ): Profile {
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

    data class EmployeeProfile(
        override val lastName: String,
        override val firstName: String,
        override val patronymic: String,
        val ranks: List<String>,
        val education: List<Education>,
        val degrees: List<Degrees>
    ): Profile {

        data class Education(
            val institutionName: String?,
            val speciality: String?,
            val qualification: String?,
        )

        data class Degrees(
            val degreeName: String,
            val scientificSpeciality: String,
            val code: String,

        )
    }
}

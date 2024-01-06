package ru.esstu.android.authorized.student.profile.state

import ru.esstu.student.profile.student.main_profile.domain.model.StudentProfile

data class StudentProfileScreenState(
    val studentInfo: StudentProfile? = null
)

package ru.esstu.android.authorized.profile.student.portfolio.state

import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioFile
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioType

data class StudentPortfolioState(
    val openAddPortfolioBottomSheet: Boolean = false,
    val type: StudentPortfolioType? = null,
    val listFiles: List<StudentPortfolioFile> = emptyList(),
    val isLoad: Boolean = true,
    val attachments: List<CachedFile> = emptyList(),

    val name: String = "",
    val date: Long? = null,
    val status: String = "",
    val theme: String = "",
    val coathor: String = "",
    val place: String = "",
    val endDate: Long? = null,
    val result: String = "",
    val exhibit: String = "",
    val typeWork: String = ""
)
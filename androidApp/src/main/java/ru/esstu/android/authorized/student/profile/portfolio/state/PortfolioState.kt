package ru.esstu.android.authorized.student.profile.portfolio.state

import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

data class PortfolioState(
    val openAddPortfolioBottomSheet: Boolean = false,
    val type: PortfolioType? = null,
    val listFiles: List<PortfolioFile> = emptyList(),
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
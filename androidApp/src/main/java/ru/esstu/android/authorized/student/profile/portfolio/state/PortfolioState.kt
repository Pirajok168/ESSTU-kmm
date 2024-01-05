package ru.esstu.android.authorized.student.profile.portfolio.state

import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioFile
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType

data class PortfolioState(
    val type: PortfolioType? = null,
    val listFiles: List<PortfolioFile> = emptyList(),
    val isLoad: Boolean = true
)
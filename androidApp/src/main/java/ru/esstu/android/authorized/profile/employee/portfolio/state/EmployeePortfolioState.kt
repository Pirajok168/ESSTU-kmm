package ru.esstu.android.authorized.profile.employee.portfolio.state


import ru.esstu.student.profile.porfolio.domain.model.EmployeePortfolioFile
import ru.esstu.student.profile.porfolio.domain.model.EmployeePortfolioType

data class EmployeePortfolioState(
    val type: EmployeePortfolioType? = null,
    val isLoad: Boolean = true,
    val listFiles: List<EmployeePortfolioFile> = emptyList()
)
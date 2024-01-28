package ru.esstu.android.authorized.profile.employee.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.profile.employee.portfolio.state.EmployeePortfolioState
import ru.esstu.domain.handleError.ErrorHandler
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.profile.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.porfolio.domain.repository.IPortfolioRepository
import ru.esstu.student.profile.porfolio.domain.model.StudentPortfolioType
import ru.esstu.student.profile.porfolio.domain.model.EmployeePortfolioType

class EmployeePortfolioViewModel : ViewModel() {

    private val portfolioRepository: IPortfolioRepository = ESSTUSdk.portfolioModule.repo
    private val errorHandler: ErrorHandler = ESSTUSdk.domainApi.errorHandler
    var state by mutableStateOf(EmployeePortfolioState())
        private set

    fun preDisplayFile(type: EmployeePortfolioType) {
        state = state.copy(type = type, listFiles = emptyList(), isLoad = true)
        viewModelScope.launch {
            errorHandler.makeRequest(request = {
                portfolioRepository.getFilesEmployeePortfolioByType(type)
            })
                .doOnSuccess {
                    withContext(Dispatchers.Main) {
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }
}
package ru.esstu.android.authorized.profile.employee.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.android.authorized.profile.employee.portfolio.state.EmployeePortfolioState
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.features.profile.porfolio.di.portfolioDi
import ru.esstu.features.profile.porfolio.domain.model.EmployeePortfolioType
import ru.esstu.features.profile.porfolio.domain.repository.IPortfolioRepository

class EmployeePortfolioViewModel : ViewModel() {
    private val di: DI by lazy { portfolioDi() }

    private val portfolioRepository: IPortfolioRepository by di.instance<IPortfolioRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()

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
package ru.esstu.android.authorized.student.profile.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.student.profile.portfolio.state.PortfolioState
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.profile.student.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.repository.IPortfolioRepository

class PortfolioViewModel: ViewModel() {

    private val portfolioRepository: IPortfolioRepository = ESSTUSdk.portfolioModule.repo

    var state by mutableStateOf(PortfolioState())
        private set

    fun preDisplayFile(type: PortfolioType){
        state = state.copy(type = type, listFiles = emptyList(), isLoad = true)
        viewModelScope.launch {
            portfolioRepository.getFilesPortfolioByType(type)
                .doOnSuccess {
                    withContext(Dispatchers.Main){
                        state = state.copy(listFiles = it, isLoad = false)
                    }
                }
        }
    }
}
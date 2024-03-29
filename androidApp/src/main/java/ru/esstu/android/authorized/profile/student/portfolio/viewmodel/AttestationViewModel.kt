package ru.esstu.android.authorized.profile.student.portfolio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.profile.student.portfolio.state.AttestationScreenState
import ru.esstu.domain.handleError.ErrorHandler
import ru.esstu.domain.ktor.domainApi
import ru.esstu.domain.utill.wrappers.doOnError
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.profile.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.porfolio.domain.repository.IPortfolioRepository

class AttestationViewModel: ViewModel() {

    private val portfolioRepository: IPortfolioRepository = ESSTUSdk.portfolioModule.repo
    private val errorHandler: ErrorHandler = ESSTUSdk.domainApi.errorHandler
    var state by mutableStateOf(AttestationScreenState())
        private set

    init {
        viewModelScope.launch {
            errorHandler.makeRequest(
                request =  {
                    portfolioRepository.getAttestation()
                }
            )
                .doOnSuccess {
                    withContext(Dispatchers.Main){
                        state = state.copy(loading = false, attestationList = it, savedAttestationList = it)
                    }
                }
                .doOnError {
                    withContext(Dispatchers.Main){
                        state = state.copy(loading = false, error = true)
                    }
                }
        }
    }

    fun onResetSort(){
        state = state.copy(attestationList = state.savedAttestationList)
    }
    fun onChangeYear(year: Int){
        state = state.copy(attestationList = state.savedAttestationList.filter { it.eduYear == year })
    }
}
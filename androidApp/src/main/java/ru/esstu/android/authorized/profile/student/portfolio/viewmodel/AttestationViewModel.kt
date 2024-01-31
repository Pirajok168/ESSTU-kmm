package ru.esstu.android.authorized.profile.student.portfolio.viewmodel

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
import ru.esstu.android.authorized.profile.student.portfolio.state.AttestationScreenState
import ru.esstu.data.web.api.model.doOnError
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.features.profile.porfolio.di.portfolioDi
import ru.esstu.features.profile.porfolio.domain.repository.IPortfolioRepository


class AttestationViewModel : ViewModel() {

    private val di: DI by lazy { portfolioDi() }

    private val portfolioRepository: IPortfolioRepository by di.instance<IPortfolioRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()

    var state by mutableStateOf(AttestationScreenState())
        private set

    init {
        viewModelScope.launch {
            errorHandler.makeRequest(
                request = {
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
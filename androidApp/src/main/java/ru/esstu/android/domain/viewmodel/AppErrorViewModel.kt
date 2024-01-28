package ru.esstu.android.domain.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.esstu.ESSTUSdk
import ru.esstu.domain.handleError.AppError
import ru.esstu.domain.handleError.ErrorProcessor
import ru.esstu.domain.ktor.domainApi

data class AppErrorState(
    val errorProcessor: AppError? = null
)
class AppErrorViewModel(
    private val errorProcessor: ErrorProcessor = ESSTUSdk.domainApi.errorProcessor
): ViewModel() {
    var state by mutableStateOf(AppErrorState())
        private set

    init {
        viewModelScope.launch {
            errorProcessor.error.collect {
                withContext(Dispatchers.Main){
                    state = state.copy(errorProcessor = it)
                }
            }
        }
    }

    fun onResetError(){
        state = state.copy(errorProcessor = null)
    }
}
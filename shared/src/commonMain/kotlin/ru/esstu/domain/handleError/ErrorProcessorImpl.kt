package ru.esstu.domain.handleError

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ErrorProcessorImpl:ErrorProcessor {

    private val _error: MutableStateFlow<AppError?> = MutableStateFlow(null)

    override val error: Flow<AppError?>
        get() = _error

    override fun reportError(appError: AppError?) {
        _error.value = appError
    }
}
package ru.esstu.domain.handleError

import kotlinx.coroutines.flow.Flow

interface ErrorProcessor {

    val error: Flow<AppError?>

    fun reportError(appError: AppError?)
}
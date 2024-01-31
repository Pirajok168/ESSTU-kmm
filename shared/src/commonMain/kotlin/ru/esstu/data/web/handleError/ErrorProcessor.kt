package ru.esstu.data.web.handleError

import kotlinx.coroutines.flow.Flow

interface ErrorProcessor {

    val error: Flow<AppError?>

    fun reportError(appError: AppError?)
}
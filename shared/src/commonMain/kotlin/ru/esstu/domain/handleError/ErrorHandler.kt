package ru.esstu.domain.handleError

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ServerErrors

typealias Request<T> = suspend () -> Response<T>
class ErrorHandler(
    private val errorProcessor: ErrorProcessor
) {



    suspend fun <T> makeRequest(
        request: Request<T>,
        blocking: Boolean = false,
    ): Response<T> {
        errorProcessor.reportError(null)
        return request().also { response ->
            (response as? Response.Error)?.let {
                when(it.error.error){
                    ServerErrors.Unauthorized -> errorProcessor.reportError(AppError.Unauthorized)
                    ServerErrors.Unresponsive -> errorProcessor.reportError(AppError.Unresponsive)
                    ServerErrors.Unknown -> errorProcessor.reportError(AppError.Unknown)
                    ServerErrors.Uncheck -> errorProcessor.reportError(AppError.Uncheck)
                    null -> {}
                }
            }
        }
    }
}
package ru.esstu.data.web.handleError


import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ServerErrors

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
                when (it.error.error) {
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
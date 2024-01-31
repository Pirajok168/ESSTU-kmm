package ru.esstu.data.web.api.model

//обертка результатов работы источников данных
sealed class Response<T>(open val data: T?, open val error: ResponseError?) {
    class Success<T>(override val data: T) : Response<T>(data, null)
    class Error<T>(override val error: ResponseError) : Response<T>(null, error)


    inline fun <R> transform(transform: (T) -> R): Response<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(this.error)
    }
}


suspend inline fun <T> Response<T>.doOnSuccess(onSuccess: (T) -> Unit): Response<T> {
    this.data?.let {
        onSuccess(it)
    }
    return this
}


inline fun <T> Response<T>.doOnError(doOnError: (ResponseError) -> Unit) =
    this.error?.let {
        doOnError(it)
    }
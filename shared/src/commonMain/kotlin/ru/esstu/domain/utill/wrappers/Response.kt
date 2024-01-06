package ru.esstu.domain.utill.wrappers

//обертка результатов работы источников данных
sealed class Response<T>(open val data: T?, open val error: ResponseError?) {
    class Success<T>(override val data: T) : Response<T>(data, null)
    class Error<T>(override val error: ResponseError) : Response<T>(null, error)
}



inline fun<T> Response<T>.doOnSuccess(onSuccess: (T) -> Unit): Response<T>{
    this.data?.let(onSuccess)
    return this
}



inline fun<T> Response<T>.doOnError(doOnError: () -> Unit) =
    this.error?.let{
        doOnError()
    }
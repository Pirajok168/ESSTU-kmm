package ru.esstu.utill.wrappers

//обертка результатов работы источников данных
sealed class Response<T>(open val data: T?, open val error: ResponseError?) {
    class Success<T>(override val data: T) : Response<T>(data, null)
    class Error<T>(override val error: ResponseError) : Response<T>(null, error)
}
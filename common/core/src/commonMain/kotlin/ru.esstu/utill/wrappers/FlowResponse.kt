package ru.esstu.utill.wrappers

//обертка результатов работы источников данных, передаваемых через coroutines flow
sealed class FlowResponse<T>(open val data: T?, open val error: ResponseError?) {
    class Success<T>(override val data: T) : FlowResponse<T>(data, null)
    class Error<T>(override val error: ResponseError) : FlowResponse<T>(null, error)
    class Loading<T>(val isLoading: Boolean = true, val progress:Float = 0f) : FlowResponse<T>(null, null)
}
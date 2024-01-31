package ru.esstu.data.web.api.model

//обертка результатов работы источников данных, передаваемых через coroutines flow
sealed class FlowResponse<T>(open val data: T?, open val error: ResponseError?) {
    class Success<T>(override val data: T, val localUri: String? = null) :
        FlowResponse<T>(data, null)

    class Error<T>(override val error: ResponseError) : FlowResponse<T>(null, error)
    class Loading<T>(val isLoading: Boolean = true, val progress: Float = 0f) :
        FlowResponse<T>(null, null)
}
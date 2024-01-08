package ru.esstu.domain.utill.wrappers

//Возникает при ошибке обработки запроса из источника
data class ResponseError(
    val code: Int? = null,
    val message: String? = null,
    val error: ServerErrors? = null
)



enum class ServerErrors {
    Unauthorized,
    Unresponsive,
    Unknown
}
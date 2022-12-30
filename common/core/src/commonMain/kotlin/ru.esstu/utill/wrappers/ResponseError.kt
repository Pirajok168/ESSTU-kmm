package ru.esstu.utill.wrappers

//Возникает при ошибке обработки запроса из источника
data class ResponseError(val code: Int? = null, val message: String? = null)
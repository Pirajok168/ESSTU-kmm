package ru.esstu.web_errors

import io.ktor.http.HttpStatusCode

sealed class ServerError: Error() {
    fun isServerFailure(): Boolean = this is Unresponsive
    object Unresponsive : ServerError() {

        fun isSuitable(code: Int): Boolean =
            code >= 500 || isAccountNotFound(code)

        fun isAccountNotFound(code: Int): Boolean =
            code == HttpStatusCode.NotFound.value
    }
}


package ru.esstu.android.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.auth.di.authorizationDi
import ru.esstu.auth.domain.model.Token
import ru.esstu.auth.domain.repo.IAuthRepository
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.handleError.ErrorHandler
import ru.esstu.features.firebase.domain.repo.IFirebaseRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class AuthState(
    val token: Token? = null,
    val isLoading: Boolean = false,
    val error: ResponseError? = null,
    val login: String = ""
)

sealed class AuthEvents {
    object SuppressError : AuthEvents()
    object RestoreSession : AuthEvents()
    data class PassLogin(val login: String) : AuthEvents()
    data class Authorise(val login: String, val Pass: String) : AuthEvents()
    data class AuthoriseEntrant(val login: String, val Pass: String) : AuthEvents()
    object AuthoriseGuest : AuthEvents()
}


class AuthViewModel : ViewModel() {

    private val di: DI by lazy { authorizationDi() }


    private val repo: IAuthRepository by di.instance<IAuthRepository>()
    private val errorHandler: ErrorHandler by di.instance<ErrorHandler>()
    private val firebaseRepo: IFirebaseRepository by di.instance<IFirebaseRepository>()

    var authState by mutableStateOf(AuthState())
        private set

    fun onEvent(event: AuthEvents) {

        when (event) {
            is AuthEvents.SuppressError -> authState = authState.copy(error = null)
            is AuthEvents.PassLogin -> onPassLogin(event.login)
            is AuthEvents.Authorise -> viewModelScope.launch {
                onAuth(
                    login = event.login,
                    pass = event.Pass
                )
            }

            is AuthEvents.AuthoriseEntrant -> viewModelScope.launch {
                onEntrantAuth(
                    login = event.login,
                    pass = event.Pass
                )
            }

            is AuthEvents.AuthoriseGuest -> viewModelScope.launch { onGuestAuth() }
            is AuthEvents.RestoreSession -> viewModelScope.launch { onRestoreSession() }
        }
    }

    private fun onPassLogin(login: String) {
        authState = authState.copy(login = login)
    }

    private suspend fun onRestoreSession() {
        authState = authState.copy(isLoading = true)
        authState =
            when (val result = errorHandler.makeRequest(request = { repo.refreshToken() })) {
                is Response.Error -> authState.copy(
                    token = null,
                    isLoading = false,
                    error = result.error
                )

                is Response.Success -> authState.copy(token = result.data, isLoading = false)
            }
    }

    private suspend fun onAuth(login: String, pass: String) {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.auth(login, pass)) {
            is Response.Error -> authState.copy(
                token = null,
                isLoading = false,
                error = result.error
            )

            is Response.Success -> {
                val firebaseToken = getFirebaseToken().data
                if (firebaseToken != null) {
                    when (errorHandler.makeRequest(request = {
                        firebaseRepo.registerFirebaseToken(
                            firebaseToken
                        )
                    })) {
                        is Response.Error -> authState.copy(token = null, isLoading = false)
                        is Response.Success -> authState.copy(
                            token = result.data,
                            isLoading = false
                        )
                    }
                } else {
                    authState.copy(token = result.data, isLoading = false)
                }
            }
        }
    }

    private suspend fun getFirebaseToken() = suspendCoroutine<Response<String>> { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful)
                continuation.resume(Response.Success(it.result))
            else
                continuation.resume(Response.Error(ResponseError(message = it.exception?.message)))
        }
    }

    private suspend fun onEntrantAuth(login: String, pass: String) {
        authState = authState.copy(isLoading = true)
        authState = when (val result =
            errorHandler.makeRequest(request = { repo.entrantAuth(login, pass) })) {
            is Response.Error -> authState.copy(
                token = null,
                isLoading = false,
                error = result.error
            )

            is Response.Success -> {
                authState.copy(token = result.data, isLoading = false)
            }
        }
    }

    private suspend fun onGuestAuth() {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.guestAuth()) {
            is Response.Error -> authState.copy(
                token = null,
                isLoading = false,
                error = result.error
            )

            is Response.Success -> authState.copy(token = result.data, isLoading = false)
        }
    }
}
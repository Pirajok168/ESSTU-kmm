package ru.esstu.android.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk2


import ru.esstu.IAuthRepository
import ru.esstu.auth.datasources.di.repoAuth

import ru.esstu.entities.Token


import ru.esstu.repoAuth
import ru.esstu.utill.wrappers.Response
import ru.esstu.utill.wrappers.ResponseError

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


class AuthViewModel(
    private val repo: IAuthRepository = ESSTUSdk2.repoAuth.authModule,
    //private val firebaseRepo: IFirebaseRepository
) : ViewModel() {
    var authState by mutableStateOf(AuthState())
        private set

    fun onEvent(event: AuthEvents) {
        when (event) {
            is AuthEvents.SuppressError -> authState = authState.copy(error = null)
            is AuthEvents.PassLogin -> onPassLogin(event.login)
            is AuthEvents.Authorise -> viewModelScope.launch { onAuth(login = event.login, pass = event.Pass) }
            is AuthEvents.AuthoriseEntrant -> viewModelScope.launch { onEntrantAuth(login = event.login, pass = event.Pass) }
            is AuthEvents.AuthoriseGuest -> viewModelScope.launch { onGuestAuth() }
            is AuthEvents.RestoreSession -> viewModelScope.launch { onRestoreSession() }
        }
    }

    private fun onPassLogin(login: String) {
        authState = authState.copy(login = login)
    }

    private suspend fun onRestoreSession() {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.refreshToken()) {
            is Response.Error -> authState.copy(token = null, isLoading = false, error = result.error)
            is Response.Success -> authState.copy(token = result.data, isLoading = false)
        }
    }

    private suspend fun onAuth(login: String, pass: String) {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.auth(login, pass)) {
            is Response.Error -> authState.copy(token = null, isLoading = false, error = result.error)
            is Response.Success -> {
                authState.copy(token = result.data, isLoading = false)
                /*when(val firebaseResult = firebaseRepo.registerFirebaseToken()){
                    is Response.Error -> authState.copy(token = null, isLoading = false, error = firebaseResult.error)
                    is Response.Success -> authState.copy(token = result.data, isLoading = false)
                }*/
            }
        }
    }

    private suspend fun onEntrantAuth(login: String, pass: String) {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.entrantAuth(login, pass)) {
            is Response.Error -> authState.copy(token = null, isLoading = false, error = result.error)
            is Response.Success -> {
                authState.copy(token = result.data, isLoading = false)
                /*when(val firebaseResult = firebaseRepo.registerFirebaseToken()){
                    is Response.Error -> authState.copy(token = null, isLoading = false, error = firebaseResult.error)
                    is Response.Success -> authState.copy(token = result.data, isLoading = false)
                }*/
            }
        }
    }

    private suspend fun onGuestAuth() {
        authState = authState.copy(isLoading = true)
        authState = when (val result = repo.guestAuth()) {
            is Response.Error -> authState.copy(token = null, isLoading = false, error = result.error)
            is Response.Success -> authState.copy(token = result.data, isLoading = false)
        }
    }
}
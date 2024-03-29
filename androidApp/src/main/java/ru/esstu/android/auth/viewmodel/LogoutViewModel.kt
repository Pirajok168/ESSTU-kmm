package ru.esstu.android.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.auth.datasources.di.repoAuth
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.repo.IAuthRepository



data class LogoutState(
    val isLoggingOut: Boolean = false,
    val isLoggedOut: Boolean = false,
)


class LogoutViewModel constructor(
    private val repo: IAuthRepository = ESSTUSdk.repoAuth.authModule,
    private val cache: ILoginDataRepository = ESSTUSdk.repoAuth.tokenDSManagerImpl
) : ViewModel() {

    val logoutFlow = repo.logoutFlow

    var state by mutableStateOf(LogoutState())
        private set

    fun logOut() {

        viewModelScope.launch {
            state = state.copy(isLoggingOut = true)
            // TODO("МОЖЕТ НЕ РАБОТАТЬ")
            if (cache.getToken() == null) {
                state = state.copy(isLoggingOut = false)
                return@launch
            }

            cache.setToken(null)
            repo.refreshToken()

            state = state.copy(isLoggingOut = false)
        }
    }
}
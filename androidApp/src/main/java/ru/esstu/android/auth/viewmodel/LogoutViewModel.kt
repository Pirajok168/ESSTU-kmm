package ru.esstu.android.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.auth.di.authorizationDi
import ru.esstu.auth.domain.repo.IAuthRepository
import ru.esstu.data.token.repository.ILoginDataRepository


data class LogoutState(
    val isLoggingOut: Boolean = false,
    val isLoggedOut: Boolean = false,
)


class LogoutViewModel : ViewModel() {
    private val di: DI by lazy { authorizationDi() }
    private val repo: IAuthRepository by di.instance<IAuthRepository>()
    private val cache: ILoginDataRepository by di.instance<ILoginDataRepository>()

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
package ru.esstu.android.domain.modules.account.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.instance
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.features.account.di.accountDi
import ru.esstu.features.account.domain.repo.IAccountInfoApiRepository
import ru.esstu.student.messaging.entities.Sender

data class AccountInfoState(
    val isLoading: Boolean = false,
    val user: Sender? = null,
    val error: ResponseError? = null,
)

sealed class AccountInfoEvents {
    object LoadUser : AccountInfoEvents()
    object ClearUser : AccountInfoEvents()
}


class AccountInfoViewModel : ViewModel() {
    private val di: DI by lazy { accountDi() }

    private val repo: IAccountInfoApiRepository by di.instance<IAccountInfoApiRepository>()
    var accountInfoState by mutableStateOf(AccountInfoState())
        private set

    fun onEvent(event: AccountInfoEvents) {
        when (event) {
            AccountInfoEvents.LoadUser -> viewModelScope.launch { onLoadUser() }
            AccountInfoEvents.ClearUser -> viewModelScope.launch { onClearUser() }
        }
    }

    init { onEvent(AccountInfoEvents.LoadUser) }

    private suspend fun onClearUser() {
        accountInfoState = accountInfoState.copy(isLoading = true)
        repo.clearUser()
        accountInfoState = accountInfoState.copy(isLoading = false, user = null)
    }

    private suspend fun onLoadUser() {
        accountInfoState = accountInfoState.copy(isLoading = true)
        accountInfoState = when (val response = repo.getUser()) {
            is Response.Error -> accountInfoState.copy(isLoading = false, user = null, error = response.error)
            is Response.Success -> accountInfoState.copy(isLoading = false, user = response.data, error = null)
        }
    }
}
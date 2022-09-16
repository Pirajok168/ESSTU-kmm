package ru.esstu.android.auth.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.auth.ui.components.AuthScreenPattern
import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel

@Composable
fun EntrantLoginScreen(
    onBackPressed: () -> Unit = {},
    onNavToPass: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState = authViewModel.authState

    AuthScreenPattern(
        title = "Личный кабинет абитуриента ВСГУТУ",
        subtitle = "Выполните авторизацию",
        textFieldLabel = "Логин",
        buttonText = "Продолжить",
        textFieldText = uiState.login,
        error = uiState.error,
        onTextFieldChange = {
            authViewModel.onEvent(AuthEvents.PassLogin(it))
            if (uiState.error != null)
                authViewModel.onEvent(AuthEvents.SuppressError)
        },
        onBackPressed = onBackPressed,
        onNavToNext = onNavToPass
    )
}
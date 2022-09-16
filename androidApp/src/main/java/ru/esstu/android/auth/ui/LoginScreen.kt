package ru.esstu.android.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.auth.ui.components.AuthScreenPattern
import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun LoginScreen(
    onBackPressed: () -> Unit = {},
    onNavToPass: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState = authViewModel.authState

    AuthScreenPattern(
        title = "Добро пожаловать в личный кабинет ВСГУТУ",
        subtitle = "Выполните авторизацию",
        textFieldLabel = "Логин",
        buttonText = "Продолжить",
        hideBackButton = true,
        error = uiState.error,
        textFieldText = uiState.login,
        onTextFieldChange = {
            authViewModel.onEvent(event = AuthEvents.PassLogin(it))
            if (uiState.error != null)
                authViewModel.onEvent(AuthEvents.SuppressError)
        },
        onBackPressed = onBackPressed,
        onNavToNext = onNavToPass
    )
}




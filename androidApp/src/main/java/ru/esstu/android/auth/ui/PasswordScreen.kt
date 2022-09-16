package ru.esstu.android.auth.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import ru.esstu.android.auth.ui.components.AuthScreenPattern
import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.auth.datasources.entities.TokenOwners

@Composable
fun PasswordScreen(
    onBackPressed: () -> Unit = {},
    onNavToStudentFlow: () -> Unit = {},
    onNavToTeacherFlow: () -> Unit = {},
    onNavToEntrantFlow: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {

    val uiState = authViewModel.authState
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (!uiState.isLoading && uiState.token != null) {
            when (uiState.token.owner) {
                is TokenOwners.Teacher -> onNavToTeacherFlow()
                is TokenOwners.Student -> onNavToStudentFlow()
                TokenOwners.Entrant -> onNavToEntrantFlow()
                else -> { /* ignored */ }
            }
            Log.i("TEST", uiState.token.owner.asString())
        }
    }

    AuthScreenPattern(
        title = "Добро пожаловать в личный кабинет ВСГУТУ",
        subtitle = "Выполните авторизацию",
        textFieldLabel = "Пароль",
        buttonText = "Авторизация",
        textFieldText = password,
        error = uiState.error,
        isLoadingState = uiState.isLoading,
        textFieldVisualTransformation = PasswordVisualTransformation(),
        onTextFieldChange = {
            password = it
            if (uiState.error != null)
                authViewModel.onEvent(AuthEvents.SuppressError)
        },
        onBackPressed = onBackPressed,
        onNavToNext = {
            authViewModel.onEvent(AuthEvents.Authorise(uiState.login, password))
        }
    )
}



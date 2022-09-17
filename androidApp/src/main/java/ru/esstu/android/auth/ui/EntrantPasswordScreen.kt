package ru.esstu.android.auth.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel

import ru.esstu.android.auth.ui.components.AuthScreenPattern
import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel
import ru.esstu.auth.entities.TokenOwners

@Composable
fun EntrantPasswordScreen(
    onBackPressed: () -> Unit = {},
    onNavToEntrantFlow: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {

    val uiState = authViewModel.authState
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (!uiState.isLoading && uiState.token != null) {
            when (uiState.token.owner) {
                TokenOwners.Entrant -> onNavToEntrantFlow()
                else -> { /* ignored */ }
            }
            Log.i("TEST", uiState.token.owner.toString())
        }
    }

    AuthScreenPattern(
        title = "Личный кабинет абитуриента ВСГУТУ",
        subtitle = "Выполните авторизацию",
        textFieldLabel = "Пароль",
        buttonText = "Авторизация",
        textFieldText = password,
        isLoadingState = uiState.isLoading,
        error = uiState.error,
        textFieldVisualTransformation = PasswordVisualTransformation(),
        onTextFieldChange = {
            password = it
            if (uiState.error != null)
                authViewModel.onEvent(AuthEvents.SuppressError)
        },
        onBackPressed = onBackPressed,
        onNavToNext = {
            authViewModel.onEvent(AuthEvents.AuthoriseEntrant(uiState.login, password))
        }
    )
}
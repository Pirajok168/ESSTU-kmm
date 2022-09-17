package ru.esstu.android.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R

import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel
import ru.esstu.auth.entities.TokenOwners

@Composable
fun Splashscreen(
    onNavToStudent: () -> Unit = {},
    onNavToTeacher: () -> Unit = {},
    onNavToEntrant: () -> Unit = {},
    onNavToGuest: () -> Unit = {},
    onFinishApp: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState = authViewModel.authState
    var isServiceUnavailable by remember { mutableStateOf(false) }
    LaunchedEffect(uiState) {
        if (!uiState.isLoading) {
            when (uiState.error) {
                null ->
                    when (uiState.token) {
                        null -> authViewModel.onEvent(event = AuthEvents.RestoreSession)
                        else -> when (uiState.token.owner) {
                            is TokenOwners.Teacher -> onNavToTeacher()
                            is TokenOwners.Student -> onNavToStudent()
                            TokenOwners.Entrant -> onNavToEntrant()
                            TokenOwners.Guest -> onNavToGuest()
                        }
                    }
                else -> {
                    if (uiState.error.code != 400 && uiState.error.code != 401)
                        isServiceUnavailable = true
                }
            }
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.weight(2.5f),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_auth_logo),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(end = 20.dp, bottom = 20.dp),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    text = "esstu.ru"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            if (uiState.isLoading)
                CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(2f))
        }
    }

    if (isServiceUnavailable)
        AlertDialog(onDismissRequest = { isServiceUnavailable = false; onFinishApp() },
            title = { Text(text = "Ошибка подключения") },
            text = { Text(text = "К сожалению сервисы сайта ВСГУТУ сейчас недоступны. Попробуйте позже.") },
            buttons = {
                TextButton(onClick = { isServiceUnavailable = false; onFinishApp() }) {
                    Text(text = "ок")
                }
            }
        )
}


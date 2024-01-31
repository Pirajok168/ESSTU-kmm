package ru.esstu.android.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ru.esstu.auth.domain.model.TokenOwners

@Composable
fun Splashscreen(
    onNavToStudent: () -> Unit = {},
    onNavToTeacher: () -> Unit = {},
    onNavToEntrant: () -> Unit = {},
    onNavToGuest: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState = authViewModel.authState
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
                else -> {}
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
}


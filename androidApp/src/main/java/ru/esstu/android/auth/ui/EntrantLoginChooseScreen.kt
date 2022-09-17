package ru.esstu.android.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R

import ru.esstu.android.auth.viewmodel.AuthEvents
import ru.esstu.android.auth.viewmodel.AuthViewModel
import ru.esstu.auth.entities.TokenOwners


@Composable
fun EntrantLoginChooseScreen(
    onBackPressed: () -> Unit = {},
    onNavToEntrant: () -> Unit = {},
    onNavToGuest: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {

    val uiState = authViewModel.authState

    LaunchedEffect(uiState) {
        if (!uiState.isLoading && uiState.token != null)
            if (uiState.token.owner == TokenOwners.Guest)
                onNavToGuest()
    }

    Box {
        IconButton(
            modifier = Modifier
                .padding(top = 26.dp, start = 12.dp),
            onClick = onBackPressed
        ) {
            Icon(
                modifier = Modifier.padding(0.dp),
                tint = MaterialTheme.colors.primary,
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                modifier = Modifier.paddingFromBaseline(128.dp),
                text = "Личный кабинет абитуриента ВСГУТУ",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(16.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "В случае если вы уже подали вступительные документы, выполните вход с данными, выданными приёмной комиссией",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(44.dp))


            Button(
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onNavToEntrant
            ) {
                Text(text = "Авторизация")
            }
            Spacer(modifier = Modifier.height(14.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "Авторизация позволит вам получить доступ к личным данным, а также к онлайн чату приемной комиссии",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            Button(
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary
                ),
                onClick = {
                    authViewModel.onEvent(AuthEvents.AuthoriseGuest)
                }
            ) {
                Text(text = "Я еще не подал документы")
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {

                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .scale(1.05f)
                        .offset(y = 38.dp, x = (-4).dp),
                    painter = painterResource(id = R.drawable.ic_auth_logo_borderless),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    alignment = Alignment.TopStart
                )

                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(30.dp))
                    if (uiState.isLoading)
                        CircularProgressIndicator()
                }
            }
        }
    }
}



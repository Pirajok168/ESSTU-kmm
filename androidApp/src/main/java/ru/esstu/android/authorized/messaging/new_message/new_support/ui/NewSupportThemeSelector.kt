package ru.esstu.android.authorized.messaging.new_message.new_support.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.esstu.android.authorized.messaging.new_message.new_support.ui.components.ThemeCard
import ru.esstu.android.authorized.messaging.new_message.new_support.viewmodel.NewSupportEvents
import ru.esstu.android.authorized.messaging.new_message.new_support.viewmodel.NewSupportViewModel

@Composable
fun NewSupportThemeSelectorScreen(
    onBackPressed: () -> Unit = {},
    viewModel: NewSupportViewModel
) {
    val uiState = viewModel.state
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            IconButton(onClick = onBackPressed, modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            }

            Column(Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Выберите тему обращения",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineLarge
                )
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    Text(
                        text = "Обращение в тех. поддержку",
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            LaunchedEffect(key1 = Unit, block = {
                if (!uiState.isThemesLoading && uiState.supportThemes.isEmpty())
                    viewModel.onEvent(NewSupportEvents.LoadThemes)
            })

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.supportThemes.isNotEmpty())
                LazyColumn(contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp, top = 8.dp)) {
                    items(uiState.supportThemes) { theme ->
                        ThemeCard(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            text = theme.name,
                            onClick = {
                                viewModel.onEvent(NewSupportEvents.PassTheme(theme))
                                onBackPressed()
                            }
                        )
                    }
                }
            else
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    if (uiState.isThemesLoading)
                        CircularProgressIndicator()
                    else if (uiState.themeLoadingError != null)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Возникла неизвестная ошибка")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.onEvent(NewSupportEvents.LoadThemes) }) {
                                Text(text = "Перезагрузить")
                            }
                        }
                }
        }

    }
}


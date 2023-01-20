package ru.esstu.android.student.messaging.new_message.new_appeal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import ru.esstu.android.student.messaging.new_message.new_appeal.viewmodel.NewAppealEvents
import ru.esstu.android.student.messaging.new_message.new_appeal.viewmodel.NewAppealViewModel
import ru.esstu.android.student.messaging.new_message.new_support.ui.components.ThemeCard

@Composable
fun NewAppealThemeSelectorScreen(
    onBackPressed: () -> Unit = {},
    viewModel: NewAppealViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState = viewModel.state

    Column(Modifier.systemBarsPadding()) {
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
                style = MaterialTheme.typography.h4
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "Обращение в ВСГУТУ",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }

        LaunchedEffect(key1 = Unit, block = {
            if (!uiState.isThemesLoading && uiState.themes.isEmpty())
                viewModel.onEvent(NewAppealEvents.LoadThemes)
        })

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.themes.isNotEmpty())
            LazyColumn(contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp, top = 8.dp)) {
                items(uiState.themes) { theme ->
                    ThemeCard(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        text = theme.name,
                        onClick = {
                            viewModel.onEvent(NewAppealEvents.PassTheme(theme))
                            onBackPressed()
                        }
                    )
                }
            }
        else
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (uiState.isThemesLoading)
                    CircularProgressIndicator()
                else if (uiState.themeLoadingError != null)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Возникда неизвестная ошибка")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onEvent(NewAppealEvents.LoadThemes) }) {
                            Text(text = "Перезагрузить")
                        }
                    }
            }
    }
}
package ru.esstu.android.student.messaging.new_message.new_dialog.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.student.messaging.messenger.dialogs.ui.components.MessengerCard
import ru.esstu.android.student.messaging.new_message.new_dialog.ui.components.SearchCard
import ru.esstu.android.student.messaging.new_message.new_dialog.viewmodel.NewDialogEvents
import ru.esstu.android.student.messaging.new_message.new_dialog.viewmodel.NewDialogViewModel

@Composable
fun NewDialogSearchScreen(
    onBackPress: () -> Unit = {},
    viewModel: NewDialogViewModel
) {

    val uiState = viewModel.state
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit, block = {
        if (!uiState.isRecentOpponentsLoading && uiState.recentOpponents.isEmpty())
            viewModel.onEvent(NewDialogEvents.LoadRecentOpponents)
    })

    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            viewModel.onEvent(NewDialogEvents.DropQuery)
        }
    })

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsWithImePadding(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.query, onValueChange = { viewModel.onEvent(NewDialogEvents.PassQuery(it)) },
                        singleLine = true,
                        placeholder = {
                            Text(text = "Поиск")
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                },
                actions = {
                    AnimatedVisibility(visible = uiState.query.any()) {
                        IconButton(onClick = { viewModel.onEvent(NewDialogEvents.DropQuery) }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            )
        }
    ) {
        if (uiState.isQueryLoading || uiState.queryPages.any() || uiState.query.isNotBlank())
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(uiState.queryPages) {index,  user ->

                    if (index == uiState.queryPages.lastIndex && !uiState.isQueryPageEndReached && !uiState.isQueryLoading)
                        viewModel.onEvent(NewDialogEvents.LoadNext)

                    Box(modifier = Modifier.clickable {
                        viewModel.onEvent(NewDialogEvents.SelectOpponent(user))
                        onBackPress()
                        viewModel.onEvent(NewDialogEvents.DropQuery)
                    }) {

                        SearchCard(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                            initials = user.initials,
                            title = user.fio,
                            subtitle = user.summary,
                            photoUri = user.photo.orEmpty()
                        )
                    }
                }
                if (uiState.isQueryLoading)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                if (uiState.queryPages.isEmpty() && !uiState.isQueryLoading)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                Text(text = "Ничего не найдено")
                            }
                        }
                    }
            }
        else {
            Column {

                if (uiState.isRecentOpponentsLoading)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                if (uiState.recentOpponents.any()) {
                    LazyColumn {

                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                                Image(
                                    modifier = Modifier
                                        .width(62.dp)
                                        .padding(top = 6.dp),
                                    contentScale = ContentScale.FillWidth,
                                    painter = painterResource(id = R.drawable.ic_new_dialog_pattern1),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Недавние", style = MaterialTheme.typography.h6)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(uiState.recentOpponents) {  user ->

                            Box(modifier = Modifier.clickable {
                                viewModel.onEvent(NewDialogEvents.SelectOpponent(user))
                                focusManager.clearFocus()
                                onBackPress()
                                viewModel.onEvent(NewDialogEvents.DropQuery)
                            }) {
                                SearchCard(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                                    initials = user.initials,
                                    title = user.fio,
                                    subtitle = user.summary,
                                    photoUri = user.photo.orEmpty()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

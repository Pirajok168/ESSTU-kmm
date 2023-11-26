package ru.esstu.android.authorized.messaging.new_message.new_dialog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import ru.esstu.android.authorized.messaging.new_message.new_dialog.ui.components.SearchCard
import ru.esstu.android.authorized.messaging.new_message.new_dialog.viewmodel.NewDialogEvents
import ru.esstu.android.authorized.messaging.new_message.new_dialog.viewmodel.NewDialogViewModel


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDialogSearchScreen(
    onBackPress: () -> Unit = {},
    viewModel: NewDialogViewModel
) {

    val uiState = viewModel.state
    val focusManager = LocalFocusManager.current
    var active by rememberSaveable { mutableStateOf(false) }
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
            .imePadding(),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Bottom)
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding(), bottom =  it.calculateBottomPadding())
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                SearchBar(
                    modifier = Modifier,
                    query = uiState.query,
                    onQueryChange = { viewModel.onEvent(NewDialogEvents.PassQuery(it))  },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = { Text("Поиск собеседника") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { active = false }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }

                    },
                ) {
                    if (uiState.isQueryLoading || uiState.queryPages.any() || uiState.query.isNotBlank()){
                        LazyColumn(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(vertical = it.calculateBottomPadding())
                        ) {
                            itemsIndexed(uiState.queryPages, key = { index, item ->  item.id}) {index,  user ->

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
                                        Text(
                                            text = "Ничего не найдено",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                        }
                    }
                }
            }


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
                    LazyColumn(
                        contentPadding = it
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(24.dp))

                            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                                Text(text = "Недавние", style = MaterialTheme.typography.titleLarge)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(uiState.recentOpponents, key = { it.id }) {  user ->

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

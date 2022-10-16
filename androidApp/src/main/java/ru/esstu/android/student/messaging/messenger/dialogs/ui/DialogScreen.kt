package ru.esstu.android.student.messaging.messenger.dialogs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.student.messaging.messenger.dialogs.ui.components.MessengerCard
import ru.esstu.android.student.messaging.messenger.dialogs.viewmodel.DialogEvents
import ru.esstu.android.student.messaging.messenger.dialogs.viewmodel.DialogViewModel

@Composable
fun DialogScreen(
    onNavToDialogChat: (dialogId: String) -> Unit = { },
    dialogViewModel: DialogViewModel = viewModel()
) {
    val uiState = dialogViewModel.dialogState

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                dialogViewModel.onEvent(DialogEvents.Reload)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LazyColumn(modifier = Modifier.fillMaxSize()) {

        itemsIndexed(uiState.items) { index, item ->

            if (index == uiState.items.lastIndex && !uiState.isEndReached && !uiState.isLoading)
                dialogViewModel.onEvent(DialogEvents.GetNext)

            var subtitle: String = if (item.opponent != item.lastMessage?.from) "вы: " else ""
            subtitle += when {
                item.lastMessage?.attachments?.any() == true -> "[Вложение]"
                item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                else -> ""
            }

            MessengerCard(
                modifier = Modifier
                    .clickable { onNavToDialogChat(item.id) }
                    .background(
                        MaterialTheme.colors.secondary.copy(
                            alpha = if (item.unreadMessageCount > 0)
                                0.1f
                            else
                                0f
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 24.dp),
                unread = item.unreadMessageCount,
                photoUri = item.opponent.photo.orEmpty(),
                initials = item.opponent.initials,
                title = item.opponent.shortFio,
                desc = item.opponent.summary,
                subtitle = subtitle
            )
        }
        item {
            if (uiState.isLoading && uiState.items.isNotEmpty())
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }

        }
    }
}
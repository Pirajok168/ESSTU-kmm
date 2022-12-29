package ru.esstu.android.student.messaging.messenger.dialogs.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.student.messaging.messenger.dialogs.ui.components.MessengerCard
import ru.esstu.android.student.messaging.messenger.dialogs.viewmodel.DialogEvents
import ru.esstu.android.student.messaging.messenger.dialogs.viewmodel.DialogsViewModel
import ru.esstu.domain.utill.workingDate.toFormatString
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogsScreen(
    onNavToDialogChat: (dialogId: String) -> Unit = { },
    dialogsViewModel: DialogsViewModel = viewModel(),
    updateTitle: (String) -> Unit,
    onEditingDialogs: (Dialog) -> Unit,
    isEditing: Boolean,
    selectedList: List<Dialog>
) {
    val uiState = dialogsViewModel.dialogState

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = uiState.title, block = {

        updateTitle(uiState.title)
    })

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                dialogsViewModel.onEvent(DialogEvents.Reload)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LazyColumn(modifier = Modifier.fillMaxSize()) {

        itemsIndexed(uiState.items, key = { index, item -> item.id }) { index, item ->

            if (index == uiState.items.lastIndex && !uiState.isEndReached && !uiState.isLoading)
                dialogsViewModel.onEvent(DialogEvents.GetNext)

            var subtitle: String = if (item.opponent != item.lastMessage?.from) "вы: " else ""
            // TODO: тоже костыль пока
            subtitle += when {
                item.lastMessage?.attachments!! > 0 == true -> "[Вложение]"
                item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                else -> ""
            }

            MessengerCard(
                modifier = Modifier
                    .animateItemPlacement()
                    .combinedClickable(
                        onClick = {
                            if (!isEditing) onNavToDialogChat(item.id)
                            else onEditingDialogs(item)
                        },
                        onLongClick = {
                            onEditingDialogs(item)
                        }
                    )
                    .background(
                        if (isEditing) {
                            MaterialTheme.colors.primary.copy(
                                alpha = if (selectedList.contains(item))
                                    0.1f
                                else
                                    0f
                            )
                        } else {
                            MaterialTheme.colors.secondary.copy(
                                alpha = if (item.unreadMessageCount > 0)
                                    0.1f
                                else
                                    0f
                            )
                        }
                    )
                    .padding(vertical = 8.dp, horizontal = 24.dp),
                unread = item.unreadMessageCount,
                photoUri = item.opponent.photo.orEmpty(),
                initials = item.opponent.initials,
                title = item.opponent.shortFio,
                desc = item.opponent.summary,
                subtitle = subtitle,
                date = item.lastMessage?.date?.toFormatString("dd MMM")
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
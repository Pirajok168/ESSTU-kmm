package ru.esstu.android.authorized.messaging.messanger.conversations.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.domain.modules.account.viewmodel.AccountInfoViewModel
import ru.esstu.android.authorized.messaging.messanger.conversations.viewmodel.ConversationEvents
import ru.esstu.android.authorized.messaging.messanger.conversations.viewmodel.ConversationViewModel
import ru.esstu.android.authorized.messaging.messanger.dialogs.ui.components.MessengerCard
import ru.esstu.android.authorized.messaging.messanger.dialogs.viewmodel.DialogEvents
import java.util.*

@Composable
fun ConversationScreen(
    onNavToConversationChat: (conversationId: Int) -> Unit = { },
    accountInfoViewModel: AccountInfoViewModel = viewModel(),
    dialogViewModel: ConversationViewModel = viewModel()
) {
    val accInfoState = accountInfoViewModel.accountInfoState
    val uiState = dialogViewModel.conversationState

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                dialogViewModel.onEvent(ConversationEvents.Reload)
            if (event == Lifecycle.Event.ON_STOP)
                dialogViewModel.onEvent(ConversationEvents.CancelObserving)

        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            dialogViewModel.onEvent(ConversationEvents.CancelObserving)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        itemsIndexed(uiState.items, key = { index, item -> item.id }) { index, item ->

            if (index == uiState.items.lastIndex && !uiState.isEndReached && !uiState.isLoading)
                dialogViewModel.onEvent(ConversationEvents.GetNext)

            var subtitle: String = if (accInfoState.user?.id == item.lastMessage?.from?.id) "вы: " else ""
            // TODO: тоже костыль пока
            subtitle += when {
                item.lastMessage?.attachments!! > 0 == true -> "[Вложение]"
                item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                else -> ""
            }

            MessengerCard(
                modifier = Modifier
                    .clickable { onNavToConversationChat(item.id) }
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(
                            alpha = if (item.unreadMessageCount > 0)
                                0.1f
                            else
                                0f
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 24.dp),
                unread = item.unreadMessageCount,
                initials = item.title.uppercase(Locale.getDefault()),
                title = item.title,
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

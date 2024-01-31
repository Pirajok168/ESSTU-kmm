package ru.esstu.android.authorized.messaging.messanger.supports.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.authorized.messaging.messanger.dialogs.ui.components.MessengerCard
import ru.esstu.android.authorized.messaging.messanger.supports.viewmodel.SupportEvents
import ru.esstu.android.authorized.messaging.messanger.supports.viewmodel.SupportViewModel
import ru.esstu.android.domain.modules.account.viewmodel.AccountInfoViewModel
import java.util.Locale

@Composable
fun SupportScreen(
    onNavToSupportChat: (conversationId: Int) -> Unit = { },
    updateTitle: (Int) -> Unit,
    accountInfoViewModel: AccountInfoViewModel = hiltViewModel(),
    supportViewModel: SupportViewModel = viewModel()
) {
    val accInfoState = accountInfoViewModel.accountInfoState
    val uiState = supportViewModel.supportState
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = uiState.title, block = {

        updateTitle(uiState.title)
    })
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                supportViewModel.onEvent(SupportEvents.Reload)
            if (event == Lifecycle.Event.ON_STOP)
                supportViewModel.onEvent(SupportEvents.CancelObserving)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            supportViewModel.onEvent(SupportEvents.CancelObserving)
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        itemsIndexed(uiState.items,  key = { index, item -> item.id }) { index, item ->

            if (index == uiState.items.lastIndex && !uiState.isEndReached && !uiState.isLoading)
                supportViewModel.onEvent(SupportEvents.GetNext)

            var subtitle: String = if (accInfoState.user?.id == item.lastMessage?.from?.id) "вы: " else ""
            subtitle += when {
                item.lastMessage?.attachments!! > 0 == true -> "[Вложение]"
                item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                else -> ""
            }

            MessengerCard(
                modifier = Modifier
                    .clickable { onNavToSupportChat(item.id) }
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
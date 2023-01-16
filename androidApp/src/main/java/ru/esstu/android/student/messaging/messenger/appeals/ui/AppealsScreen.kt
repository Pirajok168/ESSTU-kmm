package ru.esstu.android.student.messaging.messenger.appeals.ui

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ru.esstu.android.domain.modules.account.viewmodel.AccountInfoViewModel
import ru.esstu.android.student.messaging.messenger.appeals.viewmodel.AppealEvents
import ru.esstu.android.student.messaging.messenger.appeals.viewmodel.AppealsViewModel
import ru.esstu.android.student.messaging.messenger.dialogs.ui.components.MessengerCard
import ru.esstu.android.student.messaging.messenger.supports.viewmodel.SupportEvents
import java.util.*

@Composable
fun AppealsScreen(
    onNavToAppealChat: (conversationId: Int) -> Unit = { },
    accountInfoViewModel: AccountInfoViewModel = hiltViewModel(),
    appealViewModel: AppealsViewModel = hiltViewModel()
) {
    val accInfoState = accountInfoViewModel.accountInfoState
    val uiState = appealViewModel.appealsState
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                appealViewModel.onEvent(AppealEvents.Reload)
            if (event == Lifecycle.Event.ON_STOP)
                appealViewModel.onEvent(AppealEvents.CancelObserving)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            appealViewModel.onEvent(AppealEvents.CancelObserving)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        itemsIndexed(uiState.items) { index, item ->

            if (index == uiState.items.lastIndex && !uiState.isEndReached && !uiState.isLoading)
                appealViewModel.onEvent(AppealEvents.GetNext)

            var subtitle: String = if (accInfoState.user?.id == item.lastMessage?.from?.id) "вы: " else ""
            subtitle += when {
                item.lastMessage?.attachments!! > 0  == true -> "[Вложение]"
                item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                else -> ""
            }

            MessengerCard(
                modifier = Modifier
                    .clickable { onNavToAppealChat(item.id) }
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
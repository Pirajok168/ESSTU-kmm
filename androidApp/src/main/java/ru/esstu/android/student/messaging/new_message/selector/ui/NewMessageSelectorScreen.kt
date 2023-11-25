package ru.esstu.android.student.messaging.new_message.selector.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.R
import ru.esstu.android.domain.navigation.bottom_navigation.util.IconResource
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.authorized.messaging.messanger.dialogs.ui.components.MessengerCard
import ru.esstu.android.student.messaging.new_message.selector.ui.components.NewMessageCard
import ru.esstu.android.student.messaging.new_message.selector.viewmoled.NewMessageEvents
import ru.esstu.android.student.messaging.new_message.selector.viewmoled.NewMessageViewModel
import java.util.*

enum class MessageButtons(val icon: IconResource, val caption: String) {
    DIALOG(icon = IconResource.DrawableResource(R.drawable.ic_new_message_dialog), "Новый диалог"),
    TECH_SUPPORT(icon = IconResource.DrawableResource(R.drawable.ic_new_message_tech_support), "Обратиться в тех. поддержку"),
    APPEALS(icon = IconResource.DrawableResource(R.drawable.ic_new_message_info), "Обратиться к подразделению")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMessageSelectorScreen(
    onBackPressed: () -> Unit = {},
    onNavToDialogChat: (id: String) -> Unit = { },
    onNavToConversationChat: (id: String) -> Unit = { },
    onNavToNewDialog: () -> Unit = {},
    onNavToNewConversation: () -> Unit = {},
    onNavToNewTechSupport: () -> Unit = {},
    onNavToNewAllies: () -> Unit = {},
    newMessageViewModel: NewMessageViewModel = viewModel(),
) {
    val uiState = newMessageViewModel.newMessageState

    LaunchedEffect(Unit) {
        if (!uiState.isLoading) {
            if (uiState.recentDialogUsers.isEmpty())
                newMessageViewModel.onEvent(NewMessageEvents.LoadRecentDialogs)
            if (uiState.recentConversations.isEmpty())
                newMessageViewModel.onEvent(NewMessageEvents.LoadRecentConversations)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = "Новое сообщение")
                },
            )
        }) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    MessageButtons.values().forEach { button ->
                        NewMessageCard(
                            modifier = Modifier
                                .clickable {
                                    when (button) {
                                        MessageButtons.DIALOG -> onNavToNewDialog()
                                        MessageButtons.TECH_SUPPORT -> onNavToNewTechSupport()
                                        MessageButtons.APPEALS -> onNavToNewAllies()
                                    }
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .padding(vertical = 8.dp),
                            icon = button.icon,
                            text = button.caption
                        )
                    }
                    Divider(
                        Modifier
                            .padding(top = 8.dp)
                            .padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (uiState.recentDialogUsers.any())
                item {
                    Column {
                        Row(modifier = Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier.width(62.dp),
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.ic_schedule_weekday_ornament),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Недавние чаты", style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            items(uiState.recentDialogUsers, key = { item -> item.id }) { user ->
                MessengerCard(
                    photoUri = user.photo.orEmpty(),
                    modifier = Modifier
                        .clickable { onNavToDialogChat(user.id) }
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                    initials = user.initials,
                    title = user.shortFio,
                    desc = user.summary
                )
            }

            if (uiState.recentConversations.any())
                item {
                    Divider(
                        Modifier
                            .padding(top = 8.dp)
                            .padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Row(modifier = Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                modifier = Modifier.width(62.dp),
                                contentScale = ContentScale.FillWidth,
                                painter = painterResource(id = R.drawable.ic_schedule_weekday_ornament),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Обсуждения", style = MaterialTheme.typography.titleLarge)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            items(uiState.recentConversations, key = { it.id }) { item ->
                MessengerCard(
                    modifier = Modifier
                        .clickable { onNavToConversationChat(item.id.toString()) }
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                    initials = item.title.uppercase(Locale.getDefault()),
                    title = item.title,
                    subtitle = when {
                        item.lastMessage?.attachments!! > 0  == true -> "[Вложение]"
                        item.lastMessage?.message?.isNotBlank() == true -> item.lastMessage?.message
                        else -> ""
                    }.orEmpty()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SSP() {
    CompPreviewTheme {
        NewMessageSelectorScreen()
    }
}
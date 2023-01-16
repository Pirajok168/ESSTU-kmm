package ru.esstu.android.student.messaging.messenger.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.esstu.android.R
import ru.esstu.android.student.messaging.messenger.dialogs.ui.DialogsScreen
import ru.esstu.android.student.messaging.messenger.navigation.Pages.*
import ru.esstu.android.student.messaging.messenger.navigation.viewmodel.MessengerScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.esstu.android.student.messaging.messenger.conversations.ui.ConversationScreen
import ru.esstu.android.student.messaging.messenger.navigation.viewmodel.toNormalView
import ru.esstu.android.student.messaging.messenger.supports.ui.SupportScreen
@Stable
enum class Pages(val description: String) {
    @Stable DIALOGS("Диалоги"),
    @Stable CONVERSATION("Обсуждения"),
    @Stable TECH_SUPPORT("Тех. поддержка"),
    //@Stable APPEALS("Обращения"),
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MessengerScreen(
    onNavToDialogChat: (id: String) -> Unit = { },
    onNavToConversationChat: (id: Int) -> Unit = { },
    onNavToSupportChat: (id: Int) -> Unit = { },
    onNavToAppealChat: (id: Int) -> Unit = { },
    onNavToNewMessage: () -> Unit = { },
    viewModel: MessengerScreenViewModel = viewModel(),
    parentPadding: PaddingValues
) {
    var title by remember { mutableStateOf("Мессенджер") }
    val uiState = viewModel.dialogState

    Scaffold(
        modifier = Modifier
            .padding(parentPadding)
            .statusBarsPadding(),
        topBar = {
            if (!uiState.isEditing) {
                TopAppBar(
                    title = { Text(text = title) },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp
                )
            } else {
                TopAppBar(
                    title = {
                        Text(text = "${viewModel.dialogState.selectedPreviewDialog.size}")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.closeEditingMode()
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.showAlertDialog()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp,

                )
            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavToNewMessage
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_new_message),
                    contentDescription = null
                )
            }
        }) {
        if (uiState.showAlertDialog){
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialog() },
                title = {
                    Text(text = if (uiState.selectedPreviewDialog.size >= 2) "Удалить " toNormalView uiState.selectedPreviewDialog.size else "Удалить 1 чат")
                },
                text = {
                    Text(text = "Вы дейстивтельно хотите удалить все сообщения? Отменить это действие будет невозможно.")
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        OutlinedButton(
                            onClick = {
                                viewModel.dismissDialog()
                            }
                        ) {
                            Text(text = "Отмена")
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.deleteDialogs()
                            }
                        ) {
                            Text(text = "Удалить", color = Color.Red)
                        }
                    }
                }
            )
        }
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        val screens = remember {
            mutableStateOf(values().toList())
        }

        Box(contentAlignment = Alignment.BottomEnd) {

            Column(modifier = Modifier.padding(it)) {
                ScrollableTabRow(
                    backgroundColor = MaterialTheme.colors.background,
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                            color = MaterialTheme.colors.secondary
                        )
                    }
                ) {
                    screens.value.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title.description) },
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        )
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = screens.value.size,
                    state = pagerState,
                ) { index ->

                    when (screens.value[index]) {
                        DIALOGS -> DialogsScreen(
                            onNavToDialogChat = onNavToDialogChat,
                            updateTitle = {
                                title = it
                            },
                            onEditingDialogs = {
                                viewModel.addDialog(it)
                            },
                            isEditing = uiState.isEditing,
                            selectedList = uiState.selectedPreviewDialog
                        )
                        CONVERSATION -> ConversationScreen(
                            onNavToConversationChat = onNavToConversationChat
                        )
                        TECH_SUPPORT -> SupportScreen(
                            onNavToSupportChat = onNavToSupportChat
                        )
                        /*APPEALS -> DialogsScreen(
                            onNavToDialogChat = onNavToDialogChat,
                            updateTitle = {
                                title = it
                            },
                            onEditingDialogs = {
                                viewModel.addDialog(it)
                            },
                            isEditing = uiState.isEditing,
                            selectedList = uiState.selectedPreviewDialog
                        )*/
                    }
                }
            }
        }
    }
}




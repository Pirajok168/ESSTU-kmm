package ru.esstu.android.authorized.messaging.messanger

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.messanger.appeals.ui.AppealsScreen
import ru.esstu.android.authorized.messaging.messanger.conversations.ui.ConversationScreen
import ru.esstu.android.authorized.messaging.messanger.dialogs.ui.DialogsScreen
import ru.esstu.android.authorized.messaging.messanger.supports.ui.SupportScreen
import ru.esstu.android.shared.clearWindowInsets

@Stable
enum class Pages(@StringRes val descriptionId: Int) {
    @Stable
    DIALOGS(R.string.dialogs),
    @Stable
    CONVERSATION(R.string.conversation),
    @Stable
    TECH_SUPPORT(R.string.tech_help),
    @Stable
    APPEALS(R.string.appeals),
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    val screens = remember {
        mutableStateOf(Pages.values().toList())
    }
    val pagerState = rememberPagerState {
        screens.value.size
    }
    val scope = rememberCoroutineScope()
    val uiState = viewModel.dialogState
    var title by remember { mutableIntStateOf(R.string.messanger) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .padding(parentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = title)) },
                windowInsets = WindowInsets.clearWindowInsets(),
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets.clearWindowInsets(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavToNewMessage
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_new_message),
                    contentDescription = null,
                )
            }
        }
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {

            Column(modifier = Modifier.padding(it)) {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = MaterialTheme.colors.secondary
                        )
                    },
                ) {
                    screens.value.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(stringResource(id = title.descriptionId)) },
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        )
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                ) { index ->

                    when (screens.value[index]) {
                        Pages.DIALOGS -> DialogsScreen(
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
                        Pages.CONVERSATION -> ConversationScreen(
                            onNavToConversationChat = onNavToConversationChat,
                            updateTitle = {
                                title = it
                            }
                        )
                        Pages.TECH_SUPPORT -> SupportScreen(
                            onNavToSupportChat = onNavToSupportChat,
                            updateTitle = {
                                title = it
                            }
                        )
                        Pages.APPEALS -> AppealsScreen(
                            onNavToAppealChat = onNavToAppealChat,
                            updateTitle = {
                                title = it
                            }
                        )
                    }
                }
            }
        }
    }
}
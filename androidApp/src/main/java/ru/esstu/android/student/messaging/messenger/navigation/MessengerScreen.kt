package ru.esstu.android.student.messaging.messenger.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.esstu.android.R
import ru.esstu.android.student.messaging.messenger.appeals.ui.AppealScreen
import ru.esstu.android.student.messaging.messenger.conversations.ui.ConversationScreen
import ru.esstu.android.student.messaging.messenger.dialogs.ui.DialogScreen
import ru.esstu.android.student.messaging.messenger.navigation.Pages.*
import ru.esstu.android.student.messaging.messenger.supports.ui.SupportScreen


enum class Pages(val description: String) {
    DIALOGS("Диалоги"),
    CONVERSATION("Обсуждения"),
    TECH_SUPPORT("Тех. поддержка"),
    APPEALS("Обращения"),
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MessengerScreen(
    onNavToDialogChat: (id: String) -> Unit = { },
    onNavToConversationChat: (id: Int) -> Unit = { },
    onNavToSupportChat: (id: Int) -> Unit = { },
    onNavToAppealChat: (id: Int) -> Unit = { },
    onNavToNewMessage: () -> Unit = { },
    parentPadding: PaddingValues
) {

    Scaffold(
        modifier = Modifier.padding(parentPadding).statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Мессенджер") },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
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

        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
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
                    values().forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title.description) },
                            selected = pagerState.currentPage == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        )
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = values().size,
                    state = pagerState,
                ) { index ->
                     DialogScreen(onNavToDialogChat = onNavToDialogChat)
                    /*when (values()[index]) {
                        DIALOGS -> DialogScreen(onNavToDialogChat = onNavToDialogChat)
                        CONVERSATION -> ConversationScreen(onNavToConversationChat = onNavToConversationChat)
                        TECH_SUPPORT -> SupportScreen(onNavToSupportChat = onNavToSupportChat)
                       APPEALS -> AppealScreen(onNavToAppealChat = onNavToAppealChat)
                    }*/
                }
            }
        }
    }
}


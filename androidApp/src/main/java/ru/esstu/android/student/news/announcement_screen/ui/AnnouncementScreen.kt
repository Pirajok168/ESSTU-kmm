package ru.esstu.android.student.news.announcement_screen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import ru.esstu.android.student.news.announcement_screen.ui.components.NewsPage
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsEvents
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AnnouncementScreen(
    parentPadding: PaddingValues,
    onBackPressed: () -> Unit,
    onNavToImageScreen: (selected: String, uris: List<String>) -> Unit = { _, _ -> },
    viewModel: AnnouncementsViewModel = viewModel()
) {
    val uiState = viewModel.state
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize().statusBarsPadding().padding(parentPadding),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(text = "Объявления") }
            )
        }
    ) {
        LaunchedEffect(key1 = Unit, block = {
            if (!uiState.isPagesLoading && uiState.pages.isEmpty())
                viewModel.onEvent(AnnouncementsEvents.LoadAndRefresh)

        })

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(it)
        ) {
            itemsIndexed(uiState.pages) { index, announcement ->

                if (index == uiState.pages.lastIndex && !uiState.isEndReached && !uiState.isPagesLoading)
                    viewModel.onEvent(AnnouncementsEvents.LoadNext)

                Surface(
                    elevation = 4.dp,
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colors.background
                ) {
                    NewsPage(
                        horizontalPadding = 16.dp,
                        verticalPadding = 16.dp,
                        node = announcement,
                        onError = { message ->
                            scope.launch {
                                if (scaffoldState.snackbarHostState.currentSnackbarData == null)
                                    scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                            }
                        },
                        onImageClick = { selected, uris ->
                            onNavToImageScreen(selected, uris)
                        },
                        onDownloadAttachment = { _, attachment ->
                            viewModel.onEvent(AnnouncementsEvents.DownloadAttachment(attachment))
                        },
                        onUpdateAttachment = { newsId, attachment ->
                            viewModel.onEvent(AnnouncementsEvents.UpdateAttachment(attachment = attachment, announcementId = newsId))
                        }
                    )
                }
            }

            if (uiState.isPagesLoading)
                item {
                    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
        }
    }
}
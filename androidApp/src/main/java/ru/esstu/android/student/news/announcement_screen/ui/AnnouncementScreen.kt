package ru.esstu.android.student.news.announcement_screen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.esstu.android.student.news.announcement_screen.ui.components.NewsPage
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsEvents
import ru.esstu.android.student.news.announcement_screen.viewmodel.AnnouncementsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementScreen(
    parentPadding: PaddingValues,
    onBackPressed: () -> Unit,
    onNavToImageScreen: (selected: String, uris: List<String>) -> Unit = { _, _ -> },
    viewModel: AnnouncementsViewModel = viewModel()
) {
    val uiState = viewModel.state
    val scope = rememberCoroutineScope()
    //val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(parentPadding),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "Объявления")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                    shadowElevation = 4.dp,
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsPage(
                        horizontalPadding = 16.dp,
                        verticalPadding = 16.dp,
                        node = announcement,
                        onError = { message ->
                            scope.launch {
                            //    if (scaffoldState.snackbarHostState.currentSnackbarData == null)
                             //       scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
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
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
        }
    }
}
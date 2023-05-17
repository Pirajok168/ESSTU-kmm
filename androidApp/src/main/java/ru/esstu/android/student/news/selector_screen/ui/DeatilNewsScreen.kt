package ru.esstu.android.student.news.selector_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.esstu.android.student.news.announcement_screen.ui.components.NewsPage
import ru.esstu.android.student.news.selector_screen.viewModel.SelectorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailNewsScreen(
    parentPadding: PaddingValues,
    onBackPressed: () -> Unit,
    onNavToImageScreen: (selected: String, uris: List<String>) -> Unit = { _, _ -> },
    selectorViewModel: SelectorViewModel = viewModel(),
) {
    val uiState = selectorViewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, block = {
        if (uiState.node == null)
            onBackPressed()
    })

    //val scaffoldState = rememberScaffoldState()
    Scaffold(
        modifier = Modifier
            .padding(parentPadding)
            .statusBarsPadding(),

        //scaffoldState = scaffoldState,
        topBar = {
            MediumTopAppBar(
                title = {Text(text = uiState.title.ifBlank { "Запись" })},
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors =  TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            if (uiState.node != null)
                NewsPage(
                    isExpandedDefault = true,
                    horizontalPadding = 16.dp,
                    verticalPadding = 16.dp,
                    node = uiState.node,
                    onError = { message ->
                        scope.launch {
                            //if (scaffoldState.snackbarHostState.currentSnackbarData == null)
                            //    scaffoldState.snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                        }
                    },
                    onImageClick = { selected, uris ->
                        onNavToImageScreen(selected, uris)
                    },
                    onDownloadAttachment = { _, attachment ->
                        //selectorViewModel.onEvent(SelectorScreenEvents.DownloadAttachment(attachment))
                    },
                    onUpdateAttachment = { _, attachment ->
                       // selectorViewModel.onEvent(SelectorScreenEvents.UpdateAttachment(attachment = attachment))
                    }
                )
        }
    }
}
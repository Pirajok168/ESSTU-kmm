package ru.esstu.android.authorized.messaging.new_message.new_appeal.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.AddNewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.authorized.messaging.new_message.new_appeal.viewmodel.NewAppealEvents
import ru.esstu.android.authorized.messaging.new_message.new_appeal.viewmodel.NewAppealViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewAppealScreen(
    onBackPressed: () -> Unit = {},
    onNavToDepartmentSelector: () -> Unit = {},
    onNavToThemeSelector: () -> Unit = {},
    onNavToAppealChat: (chatId: Int) -> Unit = { _ -> },
    viewModel: NewAppealViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState = viewModel.state

    LaunchedEffect(key1 = Unit, block = {
        if (!uiState.isDepartmentsLoading && uiState.departments.isEmpty())
            viewModel.onEvent(NewAppealEvents.LoadDepartments)
    })

    LaunchedEffect(key1 = uiState, block = {
        if (!uiState.isNewAppealCreating && uiState.newAppeal != null)
            onNavToAppealChat(uiState.newAppeal.id)
    })
    //region activityResultApi
    val context = LocalContext.current
    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUris = result.data?.run {
                when (val uris = clipData) {
                    null ->
                        listOfNotNull(data)

                    else ->
                        (0 until uris.itemCount).mapNotNull { index -> uris.getItemAt(index).uri }
                }
            } ?: return@rememberLauncherForActivityResult

            val files = resultUris.mapNotNull { it.cacheToFile(context) }

            viewModel.onEvent(NewAppealEvents.PassAttachments(files))
        }
    }
    //endregion

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Обращение в ВСГУТУ")
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding()
                    .height(54.dp),
                enabled = !uiState.isNewAppealCreating && uiState.selectedTheme != null && uiState.message.isNotBlank(),
                onClick = {
                    viewModel.onEvent(NewAppealEvents.CreateNewAppeal)
                }
            ) {
                Text(text = "Создать")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState(),
                    reverseScrolling = true
                )
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = "Отделение", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                onClick = {
                    onNavToDepartmentSelector()
                }
            ) {
                Box(modifier = Modifier.padding(16.dp)){
                    if (uiState.selectedDepartment == null)
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                            Text(text = "Отдел не выбран", style = MaterialTheme.typography.bodyLarge)
                        }
                    else
                        Text(text = uiState.selectedDepartment.name, style = MaterialTheme.typography.bodyLarge)
                }

            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = "Тема", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                onClick = {
                    if (!uiState.isNewAppealCreating && uiState.selectedDepartment != null){
                        onNavToThemeSelector()
                    }
                }
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (uiState.selectedTheme == null)
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                            Text(text = if(uiState.isThemesLoading && uiState.themes.isEmpty()) "Загрузка" else "Тема не выбрана", style = MaterialTheme.typography.bodyLarge)
                        }
                    else
                        Text(text = uiState.selectedTheme.name, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = "Подробности", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
            ) {
                items(uiState.attachments, key = { it.uri }) { attachment ->
                    NewAttachment(
                        modifier = Modifier.animateItemPlacement(),
                        title = "${attachment.name}.${attachment.ext}",
                        uri = attachment.uri,
                        onClose = {
                            if (!uiState.isNewAppealCreating)
                                viewModel.onEvent(NewAppealEvents.RemoveAttachment(attachment))
                        }
                    )
                }

                item(key = "add") {
                    Box(modifier = Modifier.animateItemPlacement()) {
                        AddNewAttachment {
                            if (!uiState.isNewAppealCreating)
                                fileLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                                    type = "*/*"
                                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isNewAppealCreating,
                modifier = Modifier
                    .heightIn(max = 180.dp)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 8.dp),
                value = uiState.message,
                onValueChange = { msg -> viewModel.onEvent(NewAppealEvents.PassMessage(msg)) },
                placeholder = {
                    Text(text = "Описание")
                }
            )

        }
    }
}
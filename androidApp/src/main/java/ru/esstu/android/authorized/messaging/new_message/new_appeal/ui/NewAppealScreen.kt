package ru.esstu.android.authorized.messaging.new_message.new_appeal.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
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
                    Text(text = stringResource(id = R.string.appeal_esstu))
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
                Text(text = stringResource(id = R.string.create))
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
                Text(text = stringResource(id = R.string.esstu_departament), style = MaterialTheme.typography.titleLarge)
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
                            Text(text = stringResource(id = R.string.esstu_exist_departament), style = MaterialTheme.typography.bodyLarge)
                        }
                    else
                        Text(text = uiState.selectedDepartment.name, style = MaterialTheme.typography.bodyLarge)
                }

            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = stringResource(id = R.string.theme), style = MaterialTheme.typography.titleLarge)
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
                            Text(text = if(uiState.isThemesLoading && uiState.themes.isEmpty()) stringResource(
                                id = R.string.update
                            ) else stringResource(
                                id = R.string.esstu_exist_theme
                            ), style = MaterialTheme.typography.bodyLarge)
                        }
                    else
                        Text(text = uiState.selectedTheme.name, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = stringResource(id = R.string.details), style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
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
                if (uiState.attachments.isNotEmpty()){
                    item {
                        Spacer(modifier = Modifier.size(8.dp))
                    }
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
                    Text(text = stringResource(id = R.string.message_text))
                }
            )

        }
    }
}
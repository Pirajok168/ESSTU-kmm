package ru.esstu.android.authorized.messaging.new_message.new_support.ui

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
import ru.esstu.android.authorized.messaging.new_message.new_support.viewmodel.NewSupportEvents
import ru.esstu.android.authorized.messaging.new_message.new_support.viewmodel.NewSupportViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewSupportScreen(
    onBackPress: () -> Unit = {},
    onNavToSupportChat: (chatId: Int) -> Unit = { _ -> },
    onNavToThemeSelector: () -> Unit = {},
    viewModel: NewSupportViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val uiState = viewModel.state

    LaunchedEffect(key1 = uiState, block = {
        if (!uiState.isNewSupportCreating && uiState.newSupport != null)
            onNavToSupportChat(uiState.newSupport.id)
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

            viewModel.onEvent(NewSupportEvents.PassAttachments(files))
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
                    IconButton(onClick = onBackPress) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.appeal_tech_support))
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
                enabled = !uiState.isNewSupportCreating && uiState.selectedTheme != null && uiState.message.isNotBlank(),
                onClick = {
                    viewModel.onEvent(NewSupportEvents.CreateNewSupport)
                }
            ) {
                Text(text = stringResource(id = R.string.create))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(
                    rememberScrollState(),
                    reverseScrolling = true
                )
        ) {
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
                    if (!uiState.isNewSupportCreating) {
                        onNavToThemeSelector()
                    }

                }
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    if (uiState.selectedTheme == null)
                        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                            Text(
                                text = stringResource(id = R.string.esstu_exist_theme),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    else
                        Text(
                            text = uiState.selectedTheme.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
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
                            if (!uiState.isNewSupportCreating)
                                viewModel.onEvent(NewSupportEvents.RemoveAttachment(attachment))
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
                            if (!uiState.isNewSupportCreating)
                                fileLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                                    type = "*/*"
                                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isNewSupportCreating,
                modifier = Modifier
                    .heightIn(max = 216.dp)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 8.dp),
                value = uiState.message,
                onValueChange = { msg -> viewModel.onEvent(NewSupportEvents.PassMessage(msg)) },
                placeholder = {
                    Text(text = stringResource(id = R.string.message_text))
                }
            )

        }
    }
}


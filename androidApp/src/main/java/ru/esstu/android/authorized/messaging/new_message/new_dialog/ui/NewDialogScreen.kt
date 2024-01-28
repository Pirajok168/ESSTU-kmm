package ru.esstu.android.authorized.messaging.new_message.new_dialog.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.AddNewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.authorized.messaging.messanger.dialogs.ui.components.MessengerCard
import ru.esstu.android.authorized.messaging.new_message.new_dialog.ui.components.AddNewParticipantCard
import ru.esstu.android.authorized.messaging.new_message.new_dialog.viewmodel.NewDialogEvents
import ru.esstu.android.authorized.messaging.new_message.new_dialog.viewmodel.NewDialogViewModel
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewDialogScreen(
    onBackPressed: () -> Unit = {},
    onNavToSearchScreen: () -> Unit = {},
    onNavToDialogChat: (dialogId: String) -> Unit = {},
    viewModel: NewDialogViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val uiState = viewModel.state
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = uiState, block = {
        if (uiState.isDialogCreated && uiState.opponent != null) {
            focusManager.clearFocus()
            onNavToDialogChat(uiState.opponent.id)
        }
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

            viewModel.onEvent(NewDialogEvents.PassAttachment(files))
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
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.new_dilog))
                }
            )
        },

        bottomBar = {

            var buttonModifier = Modifier.fillMaxWidth()
            if (uiState.isDialogCreating)
                buttonModifier = buttonModifier.shimmer()
            Column {
                Button(
                    enabled = (uiState.attachments.any() || uiState.message.isNotBlank()) &&
                            !uiState.isDialogCreating && !uiState.isDialogCreated &&
                            uiState.opponent != null,

                    modifier = buttonModifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp)
                        .height(48.dp),

                    onClick = { viewModel.onEvent(NewDialogEvents.CreateDialog) }) {
                    Text(text = stringResource(id = R.string.create))
                }
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
            
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = stringResource(id = R.string.companion), style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))


            if (uiState.opponent == null)
                AddNewParticipantCard(
                    modifier = Modifier
                        .clickable {
                            if (!uiState.isDialogCreating) {
                                focusManager.clearFocus()
                                onNavToSearchScreen()
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    title = stringResource(id = R.string.add)
                )
            else {
                val opponent = uiState.opponent
                MessengerCard(
                    modifier = Modifier
                        .clickable {
                            if (!uiState.isDialogCreating) {
                                focusManager.clearFocus()
                                onNavToSearchScreen()
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    initials = opponent.initials,
                    title = opponent.fio,
                    photoUri = opponent.photo.orEmpty(),
                    subtitle = opponent.summary,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = stringResource(id = R.string.new_message), style = MaterialTheme.typography.titleLarge)
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
                            if (!uiState.isDialogCreating) {
                                viewModel.onEvent(NewDialogEvents.RemoveAttachment(attachment))
                            }
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
                            if (!uiState.isDialogCreating)
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
                enabled = !uiState.isDialogCreating,
                modifier = Modifier
                    .heightIn(max = 216.dp)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .padding(bottom = 8.dp),
                value = uiState.message,
                onValueChange = { msg -> viewModel.onEvent(NewDialogEvents.PassMessage(msg)) },
                placeholder = {
                    Text(text = stringResource(id = R.string.message_text))
                })

        }
    }
}


@Preview(showBackground = true)
@Composable
fun nds() {
    CompPreviewTheme {
        NewDialogScreen()
    }
}
package ru.esstu.android.student.messaging.new_message.new_support.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.AddNewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.student.messaging.new_message.new_support.viewmodel.NewSupportEvents
import ru.esstu.android.student.messaging.new_message.new_support.viewmodel.NewSupportViewModel


@OptIn(ExperimentalFoundationApi::class)
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
            .statusBarsPadding()
            .navigationBarsWithImePadding(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Обращение в тех. поддержку")
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp)
                    .height(54.dp),
                enabled = !uiState.isNewSupportCreating && uiState.selectedTheme != null && uiState.message.isNotBlank(),
                onClick = {
                    viewModel.onEvent(NewSupportEvents.CreateNewSupport)
                }
            ) {
                Text(text = "Создать")
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
                Image(
                    modifier = Modifier
                        .width(62.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_new_dialog_pattern1),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Тема", style = MaterialTheme.typography.h6)
            }
            Spacer(modifier = Modifier.height(16.dp))



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colors.background)
                    .clickable(enabled = !uiState.isNewSupportCreating) { onNavToThemeSelector() }
                    .padding(16.dp)
            ) {

                if (uiState.selectedTheme == null)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = "Тема не выбрана", style = MaterialTheme.typography.body1)
                    }
                else
                    Text(text = uiState.selectedTheme.name, style = MaterialTheme.typography.body1)

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Image(
                    modifier = Modifier
                        .width(62.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_new_dialog_pattern2),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Подробности", style = MaterialTheme.typography.h6)
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
                            if (!uiState.isNewSupportCreating)
                                viewModel.onEvent(NewSupportEvents.RemoveAttachment(attachment))
                        }
                    )
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
                    Text(text = "Описание")
                }
            )

        }
    }
}


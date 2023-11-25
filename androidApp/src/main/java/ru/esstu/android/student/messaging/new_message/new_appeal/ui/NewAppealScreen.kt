package ru.esstu.android.student.messaging.new_message.new_appeal.ui

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.AddNewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.student.messaging.new_message.new_appeal.viewmodel.NewAppealEvents
import ru.esstu.android.student.messaging.new_message.new_appeal.viewmodel.NewAppealViewModel

@OptIn(ExperimentalFoundationApi::class)
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
            .statusBarsPadding()
            .navigationBarsWithImePadding(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
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
                Image(
                    modifier = Modifier
                        .width(62.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_new_dialog_pattern1),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Отделение", style = MaterialTheme.typography.h6)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colors.background)
                    .clickable(enabled = !uiState.isNewAppealCreating) { onNavToDepartmentSelector() }
                    .padding(16.dp)
            ) {

                if (uiState.selectedDepartment == null)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = "Отдел не выбран", style = MaterialTheme.typography.body1)
                    }
                else
                    Text(text = uiState.selectedDepartment.name, style = MaterialTheme.typography.body1)

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                Image(
                    modifier = Modifier
                        .width(62.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.ic_new_conv_pattern1),
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
                    .clickable(enabled = !uiState.isNewAppealCreating && uiState.selectedDepartment != null) { onNavToThemeSelector() }
                    .padding(16.dp)
            ) {

                if (uiState.selectedTheme == null)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = if(uiState.isThemesLoading && uiState.themes.isEmpty()) "Загрузка" else "Тема не выбрана", style = MaterialTheme.typography.body1)
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
package ru.esstu.android.student.messaging.group_chat.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.parse
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.android.domain.modules.account.viewmodel.AccountInfoViewModel
import ru.esstu.android.domain.datasources.download_worker.FileDownloadWorker
import ru.esstu.android.student.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.student.messaging.dialog_chat.ui.components.ReplyPreview
import ru.esstu.android.student.messaging.dialog_chat.ui.components.SwipeableCard
import ru.esstu.android.student.messaging.dialog_chat.ui.components.TimeDivider
import ru.esstu.android.student.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.student.messaging.dialog_chat.util.withPermissions
import ru.esstu.android.student.messaging.dialog_chat.viewmodel.DialogChatEvents
import ru.esstu.android.student.messaging.group_chat.ui.components.ChatPreview
import ru.esstu.android.student.messaging.group_chat.ui.components.MessageCard
import ru.esstu.android.student.messaging.group_chat.viewmodel.GroupChatEvents
import ru.esstu.android.student.messaging.group_chat.viewmodel.GroupChatViewModel
import ru.esstu.student.messaging.dialog_chat.util.toAttachment
import ru.esstu.student.messaging.dialog_chat.util.toReplyMessage
import ru.esstu.student.messaging.entities.DeliveryStatus


private val todayYear = DateTime.now().year
private val dateFormat: DateFormat = DateFormat("d MMM yyyy")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupChatScreen(
    onBackPressed: () -> Unit = {},
    onNavToImage: (startUri: String, uris: List<String>) -> Unit = { _, _ -> },
    convId: Int,
    showConvAuthor: Boolean = true,
    viewModel: GroupChatViewModel = viewModel(),
    accInfoVM: AccountInfoViewModel = viewModel()
) {

    DisposableEffect(Unit) {
        viewModel.onEvent(GroupChatEvents.PassConversation(convId))
        onDispose {
            viewModel.onEvent(GroupChatEvents.CancelObserver)
        }
    }

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

            viewModel.onEvent(GroupChatEvents.PassAttachments(files))
        }
    }
    //endregion

    val accInfoState = accInfoVM.accountInfoState
    val uiState = viewModel.dialogChatState

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsWithImePadding(),
        scaffoldState = scaffoldState,
        topBar = {
            Column {
                TopAppBar(backgroundColor = MaterialTheme.colors.background) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            when (val conv = uiState.conversation) {
                                null -> {
                                    Box(
                                        modifier = Modifier
                                            .size(height = 42.dp, width = 180.dp)
                                            .clip(MaterialTheme.shapes.small)
                                            .shimmer()
                                            .background(Color.Gray)
                                    )
                                }
                                else -> ChatPreview(
                                    abbreviation = conv.title,
                                    title = conv.title,
                                    subtitlePrev = "Автор ",
                                    subtitle = if (showConvAuthor) conv.author?.fio ?: "Неизвестен" else ""
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = uiState.message.attachments.any(),
                    enter = slideInVertically(initialOffsetY = { it })
                ) {
                    Column {
                        Divider()
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            items(uiState.message.attachments, key = { it.uri }) { attachment ->
                                NewAttachment(
                                    modifier = Modifier
                                        .animateItemPlacement(
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                        .padding(vertical = 16.dp),
                                    title = "${attachment.name}.${attachment.ext}",
                                    uri = if (attachment.isImage) attachment.uri else null,
                                    onClose = { viewModel.onEvent(GroupChatEvents.RemoveAttachment(attachment)) }
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = uiState.message.replyMessage != null,
                    enter = slideInVertically(initialOffsetY = { it })
                ) {
                    Column {
                        Divider()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            uiState.message.replyMessage?.run {
                                ReplyPreview(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .padding(vertical = 8.dp)
                                        .weight(1f),
                                    title = from.fio,
                                    subtitle = message.ifBlank { "[Вложение]" },
                                    time = formatDate
                                )
                                IconButton(onClick = { viewModel.onEvent(GroupChatEvents.RemoveReplyMessage) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_close),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

                Divider()
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 140.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    value = uiState.message.text,
                    leadingIcon = {
                        Row {
                            IconButton(onClick = {
                                fileLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                                    type = "*/*"
                                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                })
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_attach_file),
                                    contentDescription = null
                                )
                            }
                            IconButton(onClick = {
                                fileLauncher.launch(Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                                    type = "image/*"
                                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                })
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chat_add_photo),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    placeholder = { Text(text = "Новое сообщение") },
                    trailingIcon = {

                        val isValidMessage = uiState.message.text.any() || uiState.message.attachments.any()

                        IconButton(onClick = { viewModel.onEvent(GroupChatEvents.SendMessage) }, enabled = isValidMessage) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chat_send_message),
                                contentDescription = null,
                                tint = if (isValidMessage) MaterialTheme.colors.primary else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                            )
                        }
                    },
                    onValueChange = { viewModel.onEvent(GroupChatEvents.PassMessage(it)) })
            }
        }
    )
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {

                uiState.sentMessages
                    .groupBy {  dateFormat.parse(
                        "${it.formatDate.local.dayOfMonth} " +
                                "${it.formatDate.local.month.localShortName} ${it.formatDate.local.yearInt}"
                    ) }
                    .forEach { (date, messages) ->
                        val isCurrentYear = date.year == todayYear
                        items(messages) { message ->

                            var sentMessageModifier: Modifier = Modifier
                            if (message.status == DeliveryStatus.ERRED)
                                sentMessageModifier = sentMessageModifier.clickable {
                                    viewModel.onEvent(GroupChatEvents.ResendMessage(message))
                                }

                            Row(Modifier.padding(horizontal = 24.dp)) {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    contentAlignment = Alignment.CenterEnd,
                                    modifier = sentMessageModifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                        .weight(4f)
                                ) {
                                    MessageCard(
                                        attachments = message.attachments.map { it.toAttachment() },
                                        messageText = message.text,
                                        date = message.date,
                                        sentStatus = message.status,
                                        reply = message.replyMessage?.toReplyMessage(),
                                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                                        onImageClick = { id, attachments ->
                                            onNavToImage(
                                                attachments.firstOrNull { it.id == id }?.closestUri.orEmpty(),
                                                attachments.map { it.closestUri }
                                            )
                                        },
                                        onFileContent = { file, content ->
                                            Box(modifier = Modifier.clickable {

                                                if (file.localFileUri?.isNotBlank() == true) {
                                                    val localUri = file.localFileUri.orEmpty().toUri()

                                                    try {
                                                        val intent = Intent(Intent.ACTION_VIEW)

                                                        val photoUri: Uri = if (localUri.toString().contains("content"))
                                                            localUri
                                                        else
                                                            FileProvider.getUriForFile(
                                                                context,
                                                                context.applicationContext.packageName.toString() + ".provider",
                                                                localUri.toFile()
                                                            )

                                                        intent.setDataAndType(photoUri, file.type)
                                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                                        context.startActivity(intent)

                                                    } catch (e: ActivityNotFoundException) {
                                                        scope.launch {
                                                            scaffoldState.snackbarHostState.let { snackbarState ->
                                                                if (snackbarState.currentSnackbarData == null)
                                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                                        message = "Неподдерживаемый формат файла",
                                                                        duration = SnackbarDuration.Short
                                                                    )
                                                            }

                                                        }
                                                    }
                                                }

                                            }) { content() }

                                        }
                                    )
                                }
                            }
                        }

                        if (date != uiState.pages.firstOrNull()?.formatDate?.local)
                            item {
                                TimeDivider(date = date, isCurrentYear = isCurrentYear)
                            }
                    }

                uiState.pages
                    .mapIndexed { index, message -> index to message }
                    .groupBy { dateFormat.parse(
                        "${it.second.formatDate.local.dayOfMonth} " +
                                "${it.second.formatDate.local.month.localShortName} ${it.second.formatDate.local.yearInt}"
                    ) }
                    .forEach { (date, messages) ->
                        val isCurrentYear = date.year == todayYear

                        items(messages) { (index, message) ->
                            if (index == uiState.pages.lastIndex && !uiState.isEndReached && !uiState.isPageLoading)
                                viewModel.onEvent(GroupChatEvents.NextPage)

                            val isMessageFromYou = accInfoState.user?.id == message.from.id

                            val nextMessage = uiState.pages.getOrNull(index + 1)
                            val isNextSameAuthorAndDate =
                                nextMessage?.from?.id == message.from.id && nextMessage.date == message.date

                            Row(Modifier.padding(horizontal = 24.dp)) {

                                val backgroundColor =
                                    if (isMessageFromYou)
                                        MaterialTheme.colors.primary.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colors.onBackground.copy(alpha = 0.08f)

                                val alignment = if (isMessageFromYou)
                                    Alignment.CenterEnd
                                else
                                    Alignment.CenterStart

                                if (isMessageFromYou)
                                    Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    contentAlignment = alignment,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                        .weight(4f)
                                ) {

                                    SwipeableCard(
                                        distance = 40.dp,
                                        onDragged = { viewModel.onEvent(GroupChatEvents.PassReplyMessage(message)) },
                                        backLayerContent = {
                                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                                Icon(
                                                    modifier = Modifier.alpha(it),
                                                    painter = painterResource(id = R.drawable.ic_chat_reply),
                                                    contentDescription = null
                                                )
                                            }
                                        }) {

                                        MessageCard(
                                            from = if (!isNextSameAuthorAndDate && !isMessageFromYou)
                                                message.from else null,
                                            attachments = message.attachments,
                                            messageText = message.message,
                                            date = message.date,
                                            reply = message.replyMessage,
                                            sentStatus = message.status,
                                            backgroundColor = backgroundColor, isShowStatus = isMessageFromYou,
                                            onImageClick = { id, attachments ->
                                                onNavToImage(
                                                    attachments.firstOrNull { it.id == id }?.closestUri.orEmpty(),
                                                    attachments.map { it.closestUri }
                                                )
                                            },
                                            onFileContent = { file, content ->

                                                val workManager = remember { WorkManager.getInstance(context.applicationContext) }

                                                val permissionsLauncher =
                                                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { response ->

                                                        val allPermissionsGranted =
                                                            response.all { (_, isGranted) -> isGranted }

                                                        if (allPermissionsGranted)
                                                            viewModel.onEvent(
                                                                GroupChatEvents.DownloadAttachment(
                                                                    message.id,
                                                                    file
                                                                )
                                                            )

                                                    }

                                                LaunchedEffect(Unit) {
                                                    workManager.getWorkInfosForUniqueWorkLiveData(file.id.toString()).asFlow().collectLatest { workInfos ->
                                                        workInfos.forEach { worker ->
                                                            when (worker?.state) {
                                                                WorkInfo.State.RUNNING -> {
                                                                    val progress = worker.progress.getFloat(
                                                                        FileDownloadWorker.responseParams.KEY_PROGRESS_VAL, -1f)
                                                                    if (progress >= 0) {
                                                                        val fileCopy = file.copy(loadProgress = progress)
                                                                        viewModel.onEvent(
                                                                            GroupChatEvents.UpdateAttachment(messageId = message.id, fileCopy))
                                                                    }
                                                                }
                                                                WorkInfo.State.SUCCEEDED -> {
                                                                    val loadedUri = worker.outputData.getString(
                                                                        FileDownloadWorker.responseParams.KEY_FILE_URI)

                                                                    if (file.localFileUri == null && loadedUri != null) {
                                                                        val fileCopy = file.copy(localFileUri = loadedUri, loadProgress = null)
                                                                        viewModel.onEvent(
                                                                            GroupChatEvents.UpdateAttachment(messageId = message.id, fileCopy))
                                                                    }
                                                                }
                                                                WorkInfo.State.FAILED,
                                                                WorkInfo.State.CANCELLED -> {
                                                                    if (file.loadProgress != null) {
                                                                        val fileCopy = file.copy(loadProgress = null)
                                                                        viewModel.onEvent(
                                                                            GroupChatEvents.UpdateAttachment(messageId = message.id, fileCopy))
                                                                    }
                                                                }
                                                                else -> {
                                                                    // ignored
                                                                }
                                                            }
                                                        }
                                                    }
                                                }


                                                Box(modifier = Modifier.clickable {
                                                    if (file.loadProgress == null && file.localFileUri.isNullOrBlank()) {
                                                        ru.esstu.android.student.messaging.dialog_chat.ui.withPermissions(
                                                            context = context.applicationContext,
                                                            permissions = arrayOf(
                                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                            ),
                                                            onRequest = {
                                                                permissionsLauncher.launch(
                                                                    arrayOf(
                                                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                                    )
                                                                )
                                                            },
                                                            onGranted = {
                                                                viewModel.onEvent(
                                                                    GroupChatEvents.DownloadAttachment(
                                                                        message.id,
                                                                        file
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }

                                                    if (file.localFileUri?.isNotBlank() == true) {
                                                        val localUri =
                                                            file.localFileUri.orEmpty().toUri()

                                                        try {
                                                            val intent = Intent(Intent.ACTION_VIEW)

                                                            val photoUri: Uri =
                                                                if (localUri.toString()
                                                                        .contains("content")
                                                                )
                                                                    localUri
                                                                else
                                                                    FileProvider.getUriForFile(
                                                                        context,
                                                                        context.applicationContext.packageName.toString() + ".provider",
                                                                        localUri.toFile()
                                                                    )

                                                            intent.setDataAndType(
                                                                photoUri,
                                                                file.type
                                                            )
                                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                                            context.startActivity(intent)

                                                        } catch (e: ActivityNotFoundException) {
                                                            scope.launch {
                                                                scaffoldState.snackbarHostState.let { snackbarState ->
                                                                    if (snackbarState.currentSnackbarData == null)
                                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                                            message = "Неподдерживаемый формат файла",
                                                                            duration = SnackbarDuration.Short
                                                                        )
                                                                }

                                                            }
                                                        }
                                                    }

                                                }) { content() }
                                            }
                                        )
                                    }
                                }
                                if (!isMessageFromYou)
                                    Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        item {
                            TimeDivider(date = date, isCurrentYear = isCurrentYear)
                        }
                    }

                if (uiState.isPageLoading && uiState.pages.any())
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
            }
        }
    }
}

@Preview
@Composable
fun DCS() {
    CompPreviewTheme {
        GroupChatScreen(convId = 0)
    }
}
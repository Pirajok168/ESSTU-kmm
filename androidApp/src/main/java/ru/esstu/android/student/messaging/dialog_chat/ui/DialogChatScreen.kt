package ru.esstu.android.student.messaging.dialog_chat.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
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
import androidx.core.content.ContextCompat
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.parse
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import ru.esstu.android.R
import ru.esstu.android.domain.datasources.download_worker.FileDownloadWorker
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

import ru.esstu.android.student.messaging.dialog_chat.ui.components.*
import ru.esstu.android.student.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.student.messaging.dialog_chat.viewmodel.DialogChatEvents
import ru.esstu.android.student.messaging.dialog_chat.viewmodel.DialogChatViewModel
//import ru.esstu.student.messaging.dialog_chat.util.cacheToFile
import ru.esstu.student.messaging.dialog_chat.util.toAttachment
import ru.esstu.student.messaging.dialog_chat.util.toReplyMessage
//import ru.esstu.student.messaging.dialog_chat.util.withPermissions

import ru.esstu.student.messaging.entities.DeliveryStatus


private val todayYear = DateTime.now().year
private val dateFormat: DateFormat = DateFormat("d MMM yyyy")

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun DialogChatScreen(
    onBackPressed: () -> Unit = {},
    onNavToImage: (startUri: String, uris: List<String>) -> Unit = { _, _ -> },
    opponentId: String,
    viewModel: DialogChatViewModel = hiltViewModel()
) {

    DisposableEffect(Unit) {
        viewModel.onEvent(DialogChatEvents.PassOpponent(opponentId))

        onDispose {
            viewModel.onEvent(DialogChatEvents.CancelObserver)
        }
    }

    //region activityResultApi
    val context = LocalContext.current
    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

                viewModel.onEvent(DialogChatEvents.PassAttachments(files))
            }
        }
    //endregion

    val uiState = viewModel.dialogChatState

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val workManager = remember { WorkManager.getInstance(context.applicationContext) }
    val filesPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

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
                            when (val opponent = uiState.opponent) {
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
                                    abbreviation = opponent.initials,
                                    title = opponent.fio,
                                    subtitle = opponent.summary,
                                    photoUri = opponent.photo
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
                                    onClose = {
                                        viewModel.onEvent(
                                            DialogChatEvents.RemoveAttachment(
                                                attachment
                                            )
                                        )
                                    }
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
                                    time = DateTime(date)
                                )
                                IconButton(onClick = { viewModel.onEvent(DialogChatEvents.RemoveReplyMessage) }) {
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
                                fileLauncher.launch(
                                    Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    ).apply {
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

                        val isValidMessage =
                            uiState.message.text.any() || uiState.message.attachments.any()

                        IconButton(
                            onClick = { viewModel.onEvent(DialogChatEvents.SendMessage) },
                            enabled = isValidMessage
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chat_send_message),
                                contentDescription = null,
                                tint = if (isValidMessage) MaterialTheme.colors.primary else LocalContentColor.current.copy(
                                    alpha = LocalContentAlpha.current
                                )
                            )
                        }
                    },
                    onValueChange = { viewModel.onEvent(DialogChatEvents.PassMessage(it)) })
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
                //<editor-fold desc="Только что отправленные сообщения">
                uiState.sentMessages
                    .groupBy {
                        dateFormat.parse(
                            "${it.formatDate.local.dayOfMonth} " +
                                    "${it.formatDate.local.month.localShortName} ${it.formatDate.local.yearInt}"
                        )
                    }
                    .forEach { (date, messages) ->
                        val isCurrentYear = date.year == todayYear

                        items(messages) { message ->

                            var sentMessageModifier: Modifier = Modifier
                            if (message.status == DeliveryStatus.ERRED)
                                sentMessageModifier = sentMessageModifier.clickable {
                                    viewModel.onEvent(DialogChatEvents.ResendMessage(message))
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
                                                    val localUri =
                                                        file.localFileUri.orEmpty().toUri()

                                                    try {
                                                        val intent = Intent(Intent.ACTION_VIEW)

                                                        val photoUri: Uri = if (localUri.toString()
                                                                .contains("content")
                                                        )
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
                //</editor-fold>


                uiState.pages
                    .mapIndexed { index, message -> index to message }
                    .groupBy {
                        dateFormat.parse(
                            "${it.second.formatDate.local.dayOfMonth} " +
                                    "${it.second.formatDate.local.month.localShortName} ${it.second.formatDate.local.yearInt}"
                        )
                    }
                    .forEach { (date, messages) ->
                        val isCurrentYear = date.year == todayYear

                        items(messages) { (index, message) ->
                            if (index == uiState.pages.lastIndex && !uiState.isEndReached && !uiState.isPageLoading && uiState.pages.size > 9)
                                viewModel.onEvent(DialogChatEvents.NextPage)

                            Row(Modifier.padding(horizontal = 24.dp)) {

                                val backgroundColor =
                                    if (message.from.id != opponentId)
                                        MaterialTheme.colors.primary.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colors.onBackground.copy(alpha = 0.08f)

                                val alignment = if (message.from.id == opponentId)
                                    Alignment.CenterStart
                                else
                                    Alignment.CenterEnd

                                if (message.from.id != opponentId)
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
                                        onDragged = {
                                            viewModel.onEvent(
                                                DialogChatEvents.PassReplyMessage(
                                                    message
                                                )
                                            )
                                        },
                                        backLayerContent = {
                                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                                Icon(
                                                    modifier = Modifier.alpha(it),
                                                    painter = painterResource(id = R.drawable.ic_chat_reply),
                                                    contentDescription = null
                                                )
                                            }
                                        }) {
                                        NewMessageCard(
                                            attachments = message.attachments,
                                            messageText = message.message,
                                            date = message.date,
                                            reply = message.replyMessage,
                                            sentStatus = message.status,
                                            backgroundColor = backgroundColor,
                                            isShowStatus = message.from.id != opponentId,
                                            onImageClick = { id, attachments ->
                                                onNavToImage(
                                                    attachments.firstOrNull { it.id == id }?.closestUri.orEmpty(),
                                                    attachments.map { it.closestUri }
                                                )
                                            },
                                            onFileClick = { file ->


                                                if (filesPermissionsState.permissions.all { it.status.isGranted }) {
                                                    if(file.loadProgress == null && file.localFileUri.isNullOrBlank()){
                                                        scope.launch {
                                                            workManager.getWorkInfosForUniqueWorkLiveData(
                                                                file.id.toString()
                                                            ).asFlow().collectLatest { workInfos ->
                                                                workInfos.forEach { worker ->
                                                                    when (worker?.state) {
                                                                        WorkInfo.State.RUNNING -> {
                                                                            val progress =
                                                                                worker.progress.getFloat(
                                                                                    FileDownloadWorker.responseParams.KEY_PROGRESS_VAL,
                                                                                    -1f
                                                                                )
                                                                            if (progress >= 0) {
                                                                                val fileCopy =
                                                                                    file.copy(
                                                                                        loadProgress = progress,
                                                                                        localFileUri = null
                                                                                    )
                                                                                viewModel.onEvent(
                                                                                    DialogChatEvents.UpdateAttachment(
                                                                                        messageId = message.id,
                                                                                        fileCopy
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                        WorkInfo.State.SUCCEEDED -> {
                                                                            val loadedUri =
                                                                                worker.outputData.getString(
                                                                                    FileDownloadWorker.responseParams.KEY_FILE_URI
                                                                                )

                                                                            if (file.localFileUri == null && loadedUri != null) {
                                                                                val fileCopy =
                                                                                    file.copy(
                                                                                        localFileUri = loadedUri,
                                                                                        loadProgress = null
                                                                                    )
                                                                                viewModel.onEvent(
                                                                                    DialogChatEvents.UpdateAttachment(
                                                                                        messageId = message.id,
                                                                                        fileCopy
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                        WorkInfo.State.FAILED,
                                                                        WorkInfo.State.CANCELLED -> {
                                                                            if (file.loadProgress != null) {
                                                                                val fileCopy =
                                                                                    file.copy(
                                                                                        loadProgress = null
                                                                                    )
                                                                                viewModel.onEvent(
                                                                                    DialogChatEvents.UpdateAttachment(
                                                                                        messageId = message.id,
                                                                                        fileCopy
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                        else -> {
                                                                            // ignored
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        viewModel.onEvent(
                                                            DialogChatEvents.DownloadAttachment(
                                                                message.id,
                                                                file
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    filesPermissionsState.launchMultiplePermissionRequest()
                                                    scope.launch {
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Нет разрешений"
                                                        )
                                                    }

                                                }

                                                if (file.localFileUri?.isNotBlank() == true && file.loadProgress == null){
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
                                            }
                                        )

                                    }
                                }
                                if (message.from.id == opponentId)
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

fun withPermissions(
    context: Context,
    vararg permissions: String,
    onRequest: () -> Unit,
    onGranted: () -> Unit
) {
    val hasPermissions = permissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }



    if (hasPermissions) onGranted() else onRequest()
}

package ru.esstu.android.authorized.messaging.dialog_chat.ui


//import ru.esstu.student.messaging.dialog_chat.util.cacheToFile
//import ru.esstu.student.messaging.dialog_chat.util.withPermissions

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import ru.esstu.android.R
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.MessageCard
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewAttachment
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.NewMessageCard
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.ReplyPreview
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.SwipeableCard
import ru.esstu.android.authorized.messaging.dialog_chat.ui.components.TimeDivider
import ru.esstu.android.authorized.messaging.dialog_chat.util.cacheToFile
import ru.esstu.android.authorized.messaging.dialog_chat.viewmodel.DialogChatEvents
import ru.esstu.android.authorized.messaging.dialog_chat.viewmodel.DialogChatViewModel
import ru.esstu.android.shared.component.PersonPreview
import ru.esstu.domain.utill.workingDate.toLocalDateTime
import ru.esstu.student.messaging.dialog_chat.util.toAttachment
import ru.esstu.student.messaging.dialog_chat.util.toReplyMessage
import ru.esstu.student.messaging.entities.DeliveryStatus


private val todayYear = Clock.System.now().toLocalDateTime().year

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class
)
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


    val scope = rememberCoroutineScope()
    val workManager = remember { WorkManager.getInstance(context.applicationContext) }
    val filesPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )
    val uriHandler = LocalUriHandler.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledTonalIconButton(onClick = onBackPressed) {
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
                                    else -> PersonPreview(
                                        abbreviation = opponent.initials,
                                        title = opponent.fio,
                                        subtitle = opponent.summary,
                                        photoUri = opponent.photo
                                    )
                                }
                            }
                        }
                    },
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
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
                                    time = formatDate
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
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
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
                                tint = if (isValidMessage) MaterialTheme.colorScheme.primary else LocalContentColor.current.copy(
                                    alpha = 0.5f
                                )
                            )
                        }
                    },
                    onValueChange = { viewModel.onEvent(DialogChatEvents.PassMessage(it)) })
                Spacer(modifier = Modifier.navigationBarsPadding())
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
                        it.formatDate
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
                                        date = message.formatDate,
                                        sentStatus = message.status,
                                        reply = message.replyMessage?.toReplyMessage(),
                                        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
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
                                                            // TODO("Не забыть")
                                                           /* scaffoldState.snackbarHostState.let { snackbarState ->
                                                                if (snackbarState.currentSnackbarData == null)
                                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                                        message = "Неподдерживаемый формат файла",
                                                                        duration = SnackbarDuration.Short
                                                                    )
                                                            }*/

                                                        }
                                                    }
                                                }

                                            }) { content() }

                                        }
                                    )
                                }
                            }
                        }

                        if (date != uiState.pages.firstOrNull()?.formatDate)
                            item {
                                TimeDivider(date = date, isCurrentYear = isCurrentYear)
                            }
                    }
                //</editor-fold>


                uiState.pages
                    .mapIndexed { index, message -> index to message }
                    .groupBy {
                        LocalDate(year = it.second.formatDate.year, monthNumber = it.second.formatDate.monthNumber, dayOfMonth = it.second.formatDate.dayOfMonth)
                    }
                    .forEach { (date, messages) ->
                        val isCurrentYear = date.year == todayYear

                        items(messages) { (index, message) ->
                            if (index == uiState.pages.lastIndex && !uiState.isEndReached && !uiState.isPageLoading && uiState.pages.size > 9)
                                viewModel.onEvent(DialogChatEvents.NextPage)

                            Row(Modifier.padding(horizontal = 24.dp)) {

                                val backgroundColor =
                                    if (message.from.id != opponentId)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)

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
                                            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
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
                                                uriHandler.openUri(file.fileUri)
                                            }
                                        )

                                    }
                                }
                                if (message.from.id == opponentId)
                                    Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        item {
                            TimeDivider(date = LocalDateTime(date , LocalTime(0,0,0,0)), isCurrentYear = isCurrentYear)
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



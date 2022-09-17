package ru.esstu.android.student.news.announcement_screen.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.collectLatest
import ru.esstu.android.domain.ui.component_ui.Attachment
import ru.esstu.android.domain.ui.component_ui.UserPreview
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.student.news.entities.Attachment


import ru.esstu.student.news.entities.NewsNode


private val formatterWithYear = DateFormat("dd MMMM")
private val formatter = DateFormat("dd MMMM")


@Composable
fun NewsPage(
    node: NewsNode,
    horizontalPadding: Dp = 24.dp,
    verticalPadding: Dp = 24.dp,
    isExpandedDefault:Boolean = false,
    onImageClick: (selected: String, uris: List<String>) -> Unit = { _, _ -> },
    onDownloadAttachment: (newsId: Long, attachment: Attachment) -> Unit = { _, _ -> },
    onUpdateAttachment: (newsId: Long, attachment: Attachment) -> Unit = { _, _ -> },
    onError: (message: String) -> Unit = {}
) {
    val attachments = node.attachments.filter { !it.isImage }
    val images = node.attachments.filter { it.isImage }
    val user = node.from

    var isTextExpanded by remember { mutableStateOf(isExpandedDefault) }
    var isTextOverflow by remember { mutableStateOf(false) }
    val maxTextLines = 5

    Column(Modifier.clickable(enabled = !isExpandedDefault) {
        isTextExpanded = !isTextExpanded
    }) {
        Column(
            modifier = Modifier
                .padding(top = verticalPadding)
                .padding(horizontal = horizontalPadding),
        ) {

            UserPreview(
                abbreviation = user.initials,
                title = user.fio,
                photoUri = user.photo,
                subtitle = user.summary
            )
            if (node.title.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = node.title, style = MaterialTheme.typography.body1)
            }
        }
        if (images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            ImageGridV2(rows = 3) {
                val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
                images.forEach { image ->
                    GlideImage(
                        modifier = Modifier.clickable {
                            onImageClick(image.closestUri, images.map { it.closestUri })
                        },
                        imageModel = image.closestUri,
                        circularReveal = CircularReveal(duration = 500),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(shimmer)
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = horizontalPadding),
        ) {
            if (node.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Box(contentAlignment = Alignment.BottomEnd) {
                    Text(
                        modifier = Modifier.animateContentSize(),
                        text = node.message,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isTextExpanded) Int.MAX_VALUE else maxTextLines,
                        style = MaterialTheme.typography.body2,
                        onTextLayout = { isTextOverflow = it.hasVisualOverflow }
                    )
                    if (isTextOverflow)
                        Row(Modifier.height(IntrinsicSize.Min)) {
                            Spacer(
                                modifier = Modifier
                                    .width(48.dp)
                                    .fillMaxHeight()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color.Transparent, MaterialTheme.colors.background)
                                        )
                                    )
                            )
                            Text(
                                modifier = Modifier.background(MaterialTheme.colors.background),
                                text = "Показать полностью",
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body1,
                            )
                        }
                }
            }
        }
        if (attachments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))

            attachments.forEach { att ->
                val context = LocalContext.current
                //val workManager = remember { WorkManager.getInstance(context.applicationContext) }

               /* val permissionsLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { response ->

                        val allPermissionsGranted = response.all { (_, isGranted) -> isGranted }

                        if (allPermissionsGranted)
                            onDownloadAttachment(node.id, att)

                    }*/

               /* LaunchedEffect(Unit) {
                    workManager.getWorkInfosForUniqueWorkLiveData(att.id.toString()).asFlow().collectLatest { workInfos ->
                        workInfos.forEach { worker ->
                            when (worker?.state) {
                                WorkInfo.State.RUNNING -> {
                                    val progress = worker.progress.getFloat(FileDownloadWorker.responseParams.KEY_PROGRESS_VAL, -1f)
                                    if (progress >= 0) {
                                        val fileCopy = att.copy(loadProgress = progress)
                                        onUpdateAttachment(node.id, fileCopy)
                                    }
                                }
                                WorkInfo.State.SUCCEEDED -> {
                                    val loadedUri = worker.outputData.getString(FileDownloadWorker.responseParams.KEY_FILE_URI)

                                    if (att.localFileUri == null && loadedUri != null) {
                                        val fileCopy = att.copy(localFileUri = loadedUri, loadProgress = null)
                                        onUpdateAttachment(node.id, fileCopy)

                                    }
                                }
                                WorkInfo.State.FAILED,
                                WorkInfo.State.CANCELLED -> {
                                    if (att.loadProgress != null) {
                                        val fileCopy = att.copy(loadProgress = null)
                                        onUpdateAttachment(node.id, fileCopy)
                                    }
                                }
                                else -> {
                                    // ignored
                                }
                            }
                        }
                    }
                }*/

                Attachment(
                    modifier = Modifier
                        .clickable(enabled = att.loadProgress == null)
                        {
                            /*if (att.localFileUri.isNullOrBlank()) {
                                withPermissions(
                                    context = context.applicationContext,
                                    permissions = FileDownloadWorker.requiredPermissions,
                                    onRequest = {
                                        permissionsLauncher.launch(FileDownloadWorker.requiredPermissions)
                                    },
                                    onGranted = {
                                        onDownloadAttachment(node.id, att)
                                    }
                                )
                            }*/

                            if (att.localFileUri?.isNotBlank() == true) {
                                val localUri = att.localFileUri
                                    .orEmpty()
                                    .toUri()

                                try {
                                    val intent = Intent(Intent.ACTION_VIEW)

                                    val photoUri: Uri = if (localUri
                                            .toString()
                                            .contains("content")
                                    )
                                        localUri
                                    else
                                        FileProvider.getUriForFile(
                                            context,
                                            context.applicationContext.packageName.toString() + ".provider",
                                            localUri.toFile()
                                        )

                                    intent.setDataAndType(photoUri, att.type)
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                    context.startActivity(intent)

                                } catch (e: ActivityNotFoundException) {
                                    onError("Неподдерживаемый формат файла")
                                }
                            }
                        }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = horizontalPadding),
                    fileName = "${att.name}.${att.ext}",
                    fileDesc = att.sizeFormat,
                    loadProgress = att.loadProgress,
                    isLoaded = att.localFileUri != null
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = verticalPadding)
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.End
        ) {
            val isCurrentYear = node.date.year.year == DateTime.now().year.year
            val date = if (isCurrentYear)
                node.date.format(formatter)
            else
                node.date.format(formatterWithYear)


            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = date, style = MaterialTheme.typography.subtitle2)
            }
        }
    }

}



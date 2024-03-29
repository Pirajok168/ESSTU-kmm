package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.datetime.LocalDateTime
import ru.esstu.android.R
import ru.esstu.domain.utill.workingDate.format
import ru.esstu.domain.utill.workingDate.toFormatString
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.ReplyMessage


@Composable
fun NewMessageCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),

    messageText: String,
    attachments: List<MessageAttachment>,
    reply: ReplyMessage? = null,
    sentStatus: DeliveryStatus,
    date: Long,

    isShowStatus: Boolean = true,

    onImageClick: (selectedId: Int, images: List<MessageAttachment>) -> Unit = { _, _ -> },

    onFileClick: (attachment: MessageAttachment) -> Unit = { },
){
    val images = attachments.filter { it.isImage }
    val files = attachments.minus(images)

    TimePlaceholder(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
    ) {

        Column(horizontalAlignment = Alignment.Start) {
            if (reply != null)
                ReplyPreview(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp),
                    time = reply.dateTim,
                    title = reply.from.fio,
                    subtitle = reply.message.ifBlank { "[Вложение]" })

            if (messageText.isNotBlank())
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = messageText,
                    style = MaterialTheme.typography.bodyMedium
                )

            if (images.any()) {

                var gridModifier: Modifier = Modifier
                if (messageText.isNotBlank() || reply != null)
                    gridModifier = gridModifier.padding(top = 8.dp)

                Box(
                    modifier = gridModifier,
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ImageGrid {
                        images.forEach { image ->
                            GlideImage(
                                modifier = Modifier
                                    .clickable { onImageClick(image.id, images) }
                                    .fillMaxSize(),
                                imageModel = image.fileUri,
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    if (files.isEmpty())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                        ) {
                            if (isShowStatus) {
                                val iconModifier = Modifier
                                    .height(10.dp)
                                    .padding(start = 4.dp)

                                when (sentStatus) {
                                    DeliveryStatus.DELIVERED -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_done_1),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.READ -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.SENT -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_loading),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.ERRED -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_error),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = date.toFormatString("HH:mm"),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                }
            }


            files.forEachIndexed { index, file ->


                var attachmentModifier = Modifier
                    .fillMaxWidth()
                    .widthIn(min = 120.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        onFileClick(file)
                    }

                if (index == 0) {
                    if (images.any())
                        attachmentModifier = attachmentModifier.padding(top = 4.dp)
                    if (images.isEmpty() && messageText.isNotBlank())
                        attachmentModifier = attachmentModifier.padding(top = 0.dp)
                    if (images.isEmpty() && messageText.isBlank())
                        Spacer(modifier = Modifier.height(12.dp))
                }

                Attachment(
                    modifier = attachmentModifier,
                    fileName = "${file.name.orEmpty()}.${file.ext}",
                    fileDesc = file.sizeFormat,
                    loadProgress = file.loadProgress,
                    isLoaded = file.localFileUri?.isNotBlank() == true
                )

                if (files.getOrNull(index + 1) != null){
                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                }


            }
        }

        if (!(images.any() && files.isEmpty()))
            Row(verticalAlignment = Alignment.Bottom) {
                if (isShowStatus) {
                    val iconModifier = Modifier
                        .padding(bottom = 10.dp)
                        .height(10.dp)
                        .padding(end = 4.dp)//.scale(0.8f).offset(y = (-2).dp)

                    when (sentStatus) {
                        DeliveryStatus.SENT -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_loading),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DeliveryStatus.DELIVERED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.READ -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.ERRED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_error),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    text = date.toFormatString("HH:mm"),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
    }
}


@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),

    messageText: String,
    attachments: List<MessageAttachment>,
    reply: ReplyMessage? = null,
    sentStatus: DeliveryStatus,
    date: LocalDateTime,

    isShowStatus: Boolean = true,
    onImageClick: (selectedId: Int, images: List<MessageAttachment>) -> Unit = { _, _ -> },
    // onFileClick: (attachment: Attachment) -> Unit = { },

    onFileContent: @Composable (attachment: MessageAttachment, content: @Composable () -> Unit) -> Unit = { _, cont -> cont() },
) {

    val images = attachments.filter { it.isImage }
    val files = attachments.minus(images)

    TimePlaceholder(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
    ) {

        Column(horizontalAlignment = Alignment.Start) {
            if (reply != null)
                ReplyPreview(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp),
                    time = reply.dateTim,
                    title = reply.from.fio,
                    subtitle = reply.message.ifBlank { "[Вложение]" })

            if (messageText.isNotBlank())
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = messageText,
                    style = MaterialTheme.typography.bodyLarge
                )

            if (images.any()) {

                var gridModifier: Modifier = Modifier
                if (messageText.isNotBlank() || reply != null)
                    gridModifier = gridModifier.padding(top = 8.dp)

                Box(
                    modifier = gridModifier,
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ImageGrid {
                        images.forEach { image ->
                            GlideImage(
                                modifier = Modifier
                                    .clickable { onImageClick(image.id, images) }
                                    .fillMaxSize(),
                                imageModel = image.fileUri,
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }

                    if (files.isEmpty())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                        ) {
                            if (isShowStatus) {
                                val iconModifier = Modifier
                                    .height(10.dp)
                                    .padding(start = 4.dp)

                                when (sentStatus) {
                                    DeliveryStatus.DELIVERED -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_done_1),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.READ -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.SENT -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_loading),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                    DeliveryStatus.ERRED -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_error),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = date.format("HH:mm"),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                }
            }


            files.forEachIndexed { index, file ->

                onFileContent(file) {
                    var attachmentModifier = Modifier
                        .fillMaxWidth()
                        .widthIn(min = 120.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)

                    if (index == 0) {
                        if (images.any())
                            attachmentModifier = attachmentModifier.padding(top = 4.dp)
                        if (images.isEmpty() && messageText.isNotBlank())
                            attachmentModifier = attachmentModifier.padding(top = 0.dp)
                        if (images.isEmpty() && messageText.isBlank())
                            Spacer(modifier = Modifier.height(12.dp))
                    }

                    Attachment(
                        modifier = attachmentModifier,
                        fileName = "${file.name.orEmpty()}.${file.ext}",
                        fileDesc = file.sizeFormat,
                        loadProgress = file.loadProgress,
                        isLoaded = file.localFileUri?.isNotBlank() == true
                    )

                }
            }
        }

        if (!(images.any() && files.isEmpty()))
            Row(verticalAlignment = Alignment.Bottom) {
                if (isShowStatus) {
                    val iconModifier = Modifier
                        .padding(bottom = 10.dp)
                        .height(10.dp)
                        .padding(end = 4.dp)//.scale(0.8f).offset(y = (-2).dp)

                    when (sentStatus) {
                        DeliveryStatus.SENT -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_loading),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                        DeliveryStatus.DELIVERED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.READ -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.ERRED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_error),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    text = date.format("HH:mm"),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
    }
}


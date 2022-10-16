package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.format
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.student.messaging.entities.Attachment
import ru.esstu.student.messaging.entities.DeliveryStatus
import ru.esstu.student.messaging.entities.ReplyMessage


private val formatter: DateFormat = DateFormat("HH:mm")


@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.1f),

    messageText: String,
    attachments: List<Attachment>,
    reply: ReplyMessage? = null,
    sentStatus: DeliveryStatus,
    date: Long,

    isShowStatus: Boolean = true,
    onImageClick: (selectedId: Int, images: List<Attachment>) -> Unit = { _, _ -> },
    // onFileClick: (attachment: Attachment) -> Unit = { },

    onFileContent: @Composable (attachment: Attachment, content: @Composable () -> Unit) -> Unit = { _, cont -> cont() },
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
                    time = DateTime(reply.date),
                    title = reply.from.fio,
                    subtitle = reply.message.ifBlank { "[Вложение]" })

            if (messageText.isNotBlank())
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = messageText,
                    style = MaterialTheme.typography.body2
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
                                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.5f))
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
                                        tint = MaterialTheme.colors.background
                                    )
                                    DeliveryStatus.READ -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.background
                                    )
                                    DeliveryStatus.SENT -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_loading),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.background
                                    )
                                    DeliveryStatus.ERRED -> Icon(
                                        modifier = iconModifier,
                                        painter = painterResource(id = R.drawable.ic_chat_error),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.error
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = formatter.format(date),
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.background
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
                            tint = MaterialTheme.colors.onBackground.copy(ContentAlpha.medium)
                        )
                        DeliveryStatus.DELIVERED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_1),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.READ -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_done_all_1),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary.copy(alpha = 0.6f)
                        )
                        DeliveryStatus.ERRED -> Icon(
                            modifier = iconModifier,
                            painter = painterResource(id = R.drawable.ic_chat_error),
                            contentDescription = null,
                            tint = MaterialTheme.colors.error
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                    text = formatter.format(date),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onBackground.copy(ContentAlpha.medium)
                )
            }
    }
}

@Preview(showBackground = true)
@Composable
fun sss1() {
    CompPreviewTheme {
        /*  MessageCard(
              message = Message(
                  id = 0, status = DeliveryStatus.DELIVERED,
                  message = """
              sdfks;f;lskf;lsk;lfdsl;kl;skl;dfkl;sdklf;skl;dfkl;kl;fksl;kdl;fkskfl;sk;lfd
          """,
                  from = User(id = "", summary = "", photo = null, lastName = "", firstName = "", patronymic = ""),
                  date = LocalDateTime.now(),
                  replyMessage = null,
                  attachments = listOf(
                      Attachment(
                          type = "",
                          fileUri = "https://thumbs.dreamstime.com/b/%D0%BF%D1%80%D0%B5-%D0%BF%D0%BE%D1%81%D1%8B-%D0%BA%D0%B0-%D1%81%D0%B5%D1%80-%D1%86%D0%B0-%D0%B2-%D1%8E%D0%B1-%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D1%80%D0%B0-%D1%83%D0%B3%D0%B8-60045149.jpg",
                          id = 0,
                          name = "",
                          ext = ".jpg",
                          size = 1233
                      ),
                      Attachment(
                          type = "",
                          fileUri = "https://thumbs.dreamstime.com/b/%D0%BF%D1%80%D0%B5-%D0%BF%D0%BE%D1%81%D1%8B-%D0%BA%D0%B0-%D1%81%D0%B5%D1%80-%D1%86%D0%B0-%D0%B2-%D1%8E%D0%B1-%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D1%80%D0%B0-%D1%83%D0%B3%D0%B8-60045149.jpg",
                          id = 0,
                          name = "",
                          ext = ".jpg",
                          size = 1233
                      ),
                      Attachment(
                          type = "image",
                          fileUri = "https://thumbs.dreamstime.com/b/%D0%BF%D1%80%D0%B5-%D0%BF%D0%BE%D1%81%D1%8B-%D0%BA%D0%B0-%D1%81%D0%B5%D1%80-%D1%86%D0%B0-%D0%B2-%D1%8E%D0%B1-%D0%B5%D0%BD%D0%BD%D0%BE%D1%81%D1%82%D0%B8-%D1%80%D0%B0-%D1%83%D0%B3%D0%B8-60045149.jpg",
                          id = 0,
                          name = "",
                          ext = ".jpg",
                          size = 1233
                      ),
                  )
              )
          )*/
    }
}
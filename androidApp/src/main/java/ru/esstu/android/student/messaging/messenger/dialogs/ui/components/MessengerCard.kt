package ru.esstu.android.student.messaging.messenger.dialogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.domain.ui.theme.CompPreviewTheme



@Composable
fun MessengerCard(
    modifier: Modifier = Modifier,
    initials: String,
    photoUri: String = "",
    unread: Int = 0,
    title: String,
    desc: String = "",
    subtitle: String = "",
    date: String? = null,
    onClose: (() -> Unit)? = null,

    ) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.take(2),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
                if (photoUri.isNotBlank())
                    GlideImage(
                        imageModel = photoUri,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
            }
            if (unread > 0)
                Box(
                    modifier = Modifier
                        .offset(y = (-4).dp, x = 4.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colors.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unread < 100) unread.toString() else "+", style = TextStyle(
                                color = MaterialTheme.colors.onSecondary,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.body1
                )
                if (desc.isNotBlank())
                    Text(
                        text = " | $desc",
                        modifier = Modifier.alpha(ContentAlpha.medium),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.body1
                    )
            }

            if (subtitle.isNotBlank()){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = subtitle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .padding(end = 7.dp)
                            .alignByBaseline()
                            .weight(1f),
                        style = MaterialTheme.typography.body2
                    )

                    if (date != null){
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(
                                text = date,
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium)
                                    .alignByBaseline(),
                                maxLines = 1,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }

                    }

                }

            }
                
        }

        /*if (onClose != null)
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
            }*/
    }
}

@Preview(showBackground = true)
@Composable
fun DCP() {
    CompPreviewTheme {
        Column {
            MessengerCard(
                modifier = Modifier.padding(horizontal = 24.dp),
                initials = "TB",
                desc = "Студент б668",
                unread = 99,
                title = "Иванов И. И.",
                subtitle = "Сообщewweуцeццqweение",
                date = "1672198222"
            ) {

            }
            MessengerCard(
                modifier = Modifier.padding(horizontal = 24.dp),
                initials = "TB",
                unread = 199,
                title = "Иванов И. И.",
                subtitle = "[вложение]"
            ) {

            }
        }
    }
}
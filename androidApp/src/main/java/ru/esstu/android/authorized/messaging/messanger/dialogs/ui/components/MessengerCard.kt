package ru.esstu.android.authorized.messaging.messanger.dialogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.take(2),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
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
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unread < 100) unread.toString() else "+", style = TextStyle(
                                color = MaterialTheme.colorScheme.onSecondary,
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
                    style = MaterialTheme.typography.bodyLarge
                )
                if (desc.isNotBlank())
                    Text(
                        text = " | $desc",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Light
                        )
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
                            .padding(end = 7.dp)
                            .alignByBaseline()
                            .weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light
                        )
                    )

                    if (date != null){
                        Text(
                            text = date,
                            modifier = Modifier
                                .alignByBaseline(),
                            maxLines = 1,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Light
                            )
                        )
                    }

                }

            }
                
        }

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
package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import ru.esstu.domain.utill.workingDate.format
import ru.esstu.domain.utill.workingDate.toLocalDateTime


@Composable
fun ReplyPreview(
    modifier: Modifier = Modifier,
    time: LocalDateTime,
    title: String,
    subtitle: String
) {
    @Composable
    fun PreviewPlaceHolder(initials: String) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials.uppercase().take(2),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background)
        }
    }

    Row(modifier = modifier.height(IntrinsicSize.Min)) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    val localDate = remember { Clock.System.now().toLocalDateTime() }
                    Text(
                        text = when {
                            localDate.year == time.year && localDate.dayOfYear == time.dayOfYear ->
                                time.format("HH:mm")
                            localDate.year == time.year && localDate.dayOfYear != time.dayOfYear ->
                                time.format("dd MMMM")
                            else ->
                                time.format("dd MMMM yyyy")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }

    }
}

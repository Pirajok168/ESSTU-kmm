package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.student.messaging.entities.Sender

private val yearFormatter: DateFormat = DateFormat("dd MMMM yyyy")
private val dateFormatter: DateFormat = DateFormat("dd MMMM")
private val timeFormatter: DateFormat = DateFormat("HH:mm")

@Composable
fun ReplyPreview(
    modifier: Modifier = Modifier,
    time: DateTime,
    title: String,
    subtitle: String
) {
    @Composable
    fun PreviewPlaceHolder(initials: String) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials.uppercase().take(2), style = MaterialTheme.typography.h6.copy(fontSize = 14.sp), color = MaterialTheme.colors.background)
        }
    }

    Row(modifier = modifier.height(IntrinsicSize.Min)) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.primary)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    val localDate = remember { DateTime.now() }
                    Text(
                        text = when {
                            localDate.year == time.year && localDate.dayOfYear == time.dayOfYear -> timeFormatter.format(time.local)
                            localDate.year == time.year && localDate.dayOfYear != time.dayOfYear -> dateFormatter.format(time.local)
                            else -> yearFormatter.format(time.local)
                        },
                        style = MaterialTheme.typography.body1.copy(fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                    text = title,
                    style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.body2, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun UP1P() {
    CompPreviewTheme {
        val opponent = Sender(
            id = "",
            patronymic = "Попович",
            firstName = "Анатолий",
            lastName = "Терентьев",
            photo = null,//"https://media.wired.com/photos/5fb70f2ce7b75db783b7012c/master/w_2560%2Cc_limit/Gear-Photos-597589287.jpg",
            summary = "Студент Б660"
        )

        ReplyPreview(
            Modifier.padding(16.dp),
            title = opponent.fio,
            subtitle = "https://media.wired.com/photos/5fb70f2ce7b75db783b7012c/master/w_2560%2Cc_limit/Gear-Photos-597589287.jpg",

            time = DateTime.now()
        )
    }
}
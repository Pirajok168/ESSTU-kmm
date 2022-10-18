package ru.esstu.android.student.messaging.group_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.student.messaging.entities.Sender

@Composable
fun ChatPreview(
    modifier: Modifier = Modifier,
    abbreviation: String,
    photoUri: String? = null,
    title: String,
    useDoubleSpacing:Boolean = true,
    subtitlePrev:String? = null,
    subtitle: String = ""
) {
    @Composable
    fun PreviewPlaceHolder(initials: String) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials.uppercase().take(2), style = MaterialTheme.typography.h6.copy(fontSize = 16.sp), color = MaterialTheme.colors.background)
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        when (photoUri) {
            null ->
                PreviewPlaceHolder(initials = abbreviation)

            else ->
                GlideImage(modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                    imageModel = photoUri,
                    contentScale = ContentScale.Crop,
                    loading = { PreviewPlaceHolder(initials = abbreviation) },
                    failure = { PreviewPlaceHolder(initials = abbreviation) }
                )
        }
        if(useDoubleSpacing)
            Spacer(modifier = Modifier.width(8.dp))
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = title, style = MaterialTheme.typography.body1.copy(fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle.isNotBlank())
                Row {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = subtitlePrev.orEmpty(), style = MaterialTheme.typography.body2.copy(fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(text = subtitle, style = MaterialTheme.typography.body2.copy(fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun UPP() {
    CompPreviewTheme {
        val opponent = Sender(
            id = "",
            patronymic = "Попович",
            firstName = "Анатолий",
            lastName = "Терентьев",
            photo = null,//"https://media.wired.com/photos/5fb70f2ce7b75db783b7012c/master/w_2560%2Cc_limit/Gear-Photos-597589287.jpg",
            summary = "Студент Б660"
        )

        ChatPreview(
            Modifier.padding(16.dp),
            abbreviation = opponent.initials,
            title = opponent.fio,
            subtitle = opponent.summary,
            photoUri = opponent.photo
        )
    }
}
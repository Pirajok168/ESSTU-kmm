package ru.esstu.android.shared.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.domain.ui.theme.CompPreviewTheme
import ru.esstu.student.messaging.entities.Sender

@Composable
fun PersonPreview(
    modifier: Modifier = Modifier,
    abbreviation: String,
    photoUri: String? = null,
    title: String,
    subtitle: String = ""
) {
    @Composable
    fun PreviewPlaceHolder(initials: String) {
        Surface(
            modifier = Modifier
                .size(48.dp),
            shape = CircleShape,
            tonalElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.uppercase().take(2),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
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
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotBlank())
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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

        PersonPreview(
            Modifier.padding(16.dp),
            abbreviation = opponent.initials,
            title = opponent.fio,
            subtitle = opponent.summary,
            photoUri = opponent.photo
        )
    }
}
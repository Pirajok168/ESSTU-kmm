package ru.esstu.android.authorized.messaging.new_message.new_dialog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.esstu.android.domain.ui.theme.EsstuTheme

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    initials: String,
    photoUri: String = "",

    title: String,
    subtitle: String = "",
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(60.dp),
            shape = CircleShape,
            tonalElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.take(2),
                    style = MaterialTheme.typography.titleLarge,
                )
                if (photoUri.isNotBlank())
                    GlideImage(
                        imageModel = photoUri,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
        Column() {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = subtitle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .padding(end = 7.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchCard() {
    EsstuTheme() {
        SearchCard(initials = "ЕД", title = "Еремин Данила Александрович", subtitle = "Студент 3 курса" )
    }
}
package ru.esstu.android.authorized.messaging.group_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage


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
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.uppercase().take(2),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
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
            Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle.isNotBlank())
                Row {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(text = subtitlePrev.orEmpty(), style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

        }

    }
}

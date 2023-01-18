package ru.esstu.android.student.messaging.new_message.new_dialog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        Spacer(modifier = Modifier.size(16.dp))
        Column() {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.body1
            )

            Text(
                text = subtitle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .padding(end = 7.dp),
                style = MaterialTheme.typography.body2
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
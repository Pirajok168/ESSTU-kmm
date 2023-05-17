package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import com.valentinilk.shimmer.shimmer
import ru.esstu.student.news.entities.Creator


@Composable
private fun WhiteUserPreview(
    modifier: Modifier = Modifier,
    abbreviation: String,
    photoUri: String? = null,
    title: String,
    subtitle: String = ""
) {
    @Composable
    fun PreviewPlaceHolder(initials: String) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials.uppercase().take(2), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.background)
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        when (photoUri) {
            null ->
                PreviewPlaceHolder(initials = abbreviation)

            else ->
                GlideImage(modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                    imageModel = photoUri,
                    contentScale = ContentScale.Crop,
                    loading = { PreviewPlaceHolder(initials = abbreviation) },
                    failure = { PreviewPlaceHolder(initials = abbreviation) }
                )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.background
            )
            if (subtitle.isNotBlank())

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.background
                )


        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePreviewCard(
    modifier: Modifier = Modifier,
    photoUri: String,
    user: Creator,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(width = 268.dp, height = 240.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Box {
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                imageModel = photoUri,
                circularReveal = CircularReveal(duration = 500),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )
                }
            )

            Column {
                val alpha = 0.8f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.onBackground.copy(alpha =alpha), Color.Transparent)
                            )
                        )
                )
           //     Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.onBackground.copy(alpha = alpha))
                            )
                        )
                )
            }

            Column(Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.background)
                Spacer(modifier = Modifier.weight(1f))
                WhiteUserPreview(
                    abbreviation = user.initials,
                    title = user.fio,
                    photoUri = user.photo,
                    subtitle = user.summary
                )
            }
        }
    }
}
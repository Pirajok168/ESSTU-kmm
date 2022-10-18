package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
                style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.background
            )
            if (subtitle.isNotBlank())

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.background
                )


        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
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
        elevation = 8.dp,
        color = MaterialTheme.colors.background,
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
                                colors = listOf(MaterialTheme.colors.onBackground.copy(alpha =alpha), Color.Transparent)
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
                                colors = listOf(Color.Transparent, MaterialTheme.colors.onBackground.copy(alpha = alpha))
                            )
                        )
                )
            }

            Column(Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.body1, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colors.background)
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
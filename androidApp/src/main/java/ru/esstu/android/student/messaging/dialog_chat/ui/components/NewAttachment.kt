package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun NewAttachment(modifier: Modifier = Modifier, title: String, uri: String? = null, onClose: () -> Unit = {}) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_chat_attachment),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground
                )
                if (uri != null)
                    GlideImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = uri,
                        contentScale = ContentScale.Crop
                    )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = 4.dp, y = (-4).dp)
                    .clip(CircleShape)
                    .clickable { onClose() }
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.secondary),
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_chat_cancel_1),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 88.dp),
            text = title,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Pppppp() {
    CompPreviewTheme {
        Box(modifier = Modifier.padding(16.dp))
        {
            NewAttachment(title = "test.txt")
        }
    }
}
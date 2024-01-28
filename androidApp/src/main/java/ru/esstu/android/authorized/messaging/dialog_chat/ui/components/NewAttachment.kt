package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
        Box(Modifier, contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_chat_attachment),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
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
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_chat_cancel_1),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 40.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall,
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
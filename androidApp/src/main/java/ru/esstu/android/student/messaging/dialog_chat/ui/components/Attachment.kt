package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun Attachment(modifier: Modifier = Modifier, fileName: String, fileDesc: String, loadProgress: Float?, isLoaded: Boolean = false) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary/*.onBackground.copy(alpha = ContentAlpha.high)*/),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isLoaded)
                            R.drawable.ic_chat_attachment
                        else
                            if (!isLoaded && loadProgress == null)
                                R.drawable.ic_chat_download
                            else
                                R.drawable.ic_chat_loading
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colors.background.copy(alpha = ContentAlpha.high)
                )

                if (loadProgress != null)
                    CircularProgressIndicator(progress = loadProgress, color = MaterialTheme.colors.background.copy(alpha = ContentAlpha.high))
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(text = fileName, style = MaterialTheme.typography.body1.copy(fontSize = 14.sp), maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (fileDesc.isNotBlank())
                Text(text = fileDesc, style = MaterialTheme.typography.body2.copy(fontSize = 14.sp), maxLines = 1)
        }
    }
}

@Preview
@Composable
fun ACP() {
    CompPreviewTheme {
        Attachment(fileName = "test.txt", fileDesc = "200mb", loadProgress = 0.3f)
    }
}
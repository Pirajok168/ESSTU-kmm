package ru.esstu.android.student.messaging.new_message.new_dialog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun AddNewParticipantCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = ""
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colors.onBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(text = title, overflow = TextOverflow.Ellipsis, maxLines = 1, style = MaterialTheme.typography.body1)
            if (subtitle.isNotBlank())
                Text(text = subtitle, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.alpha(ContentAlpha.medium), style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun onnpc() {
    CompPreviewTheme {
        AddNewParticipantCard(modifier = Modifier.padding(24.dp), title = "Выберите собеседника")
    }
}
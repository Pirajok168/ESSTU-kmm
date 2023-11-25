package ru.esstu.android.authorized.messaging.new_message.new_dialog.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
        Surface(
            modifier = Modifier
                .size(60.dp),
            shape = CircleShape,
            tonalElevation = 16.dp
        ){
            Box(
                modifier = Modifier
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
        Column {
            Text(text = title, overflow = TextOverflow.Ellipsis, maxLines = 1, style = MaterialTheme.typography.bodyLarge)
            if (subtitle.isNotBlank())
                Text(text = subtitle, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.alpha(ContentAlpha.medium), style = MaterialTheme.typography.bodyMedium)
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
package ru.esstu.android.student.messaging.new_message.selector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.esstu.android.domain.navigation.bottom_navigation.util.IconResource
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun NewMessageCard(modifier: Modifier = Modifier, icon: IconResource, text: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.high)
                ),
            contentAlignment = Alignment.Center
        ) {
            icon.AsIcon(MaterialTheme.colors.secondary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}


@Preview(showBackground = true)
@Composable
fun NMP() {
    CompPreviewTheme {
        NewMessageCard(icon = IconResource.VectorResource(vector = Icons.Default.Warning), text = "тест")
    }
}
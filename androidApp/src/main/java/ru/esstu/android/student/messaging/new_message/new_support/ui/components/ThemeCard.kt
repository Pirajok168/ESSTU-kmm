package ru.esstu.android.student.messaging.new_message.new_support.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme


@Composable
fun ThemeCard(modifier: Modifier = Modifier, text: String, enabled: Boolean = true, onClick: () -> Unit = {}) {

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
        onClick = {
            if (enabled) {
                onClick()
            }

        }
    ) {
        Box(modifier = Modifier.padding(16.dp)){
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ssssss() {
    CompPreviewTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            ThemeCard(text = "Группа Б666")
        }
    }
}
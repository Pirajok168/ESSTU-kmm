package ru.esstu.android.student.messaging.new_message.new_support.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    Row(
        modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),

    ) {
        Image(
            modifier = Modifier.width(60.dp).padding(top = 6.dp),
            painter = painterResource(id = R.drawable.ic_new_support_card_ornament),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.body1)
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
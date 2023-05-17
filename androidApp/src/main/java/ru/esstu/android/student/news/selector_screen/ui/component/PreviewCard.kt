package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.esstu.android.domain.ui.component_ui.UserPreview
import ru.esstu.student.news.entities.Creator



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewCard(
    modifier: Modifier = Modifier,
    user: Creator,
    title: String,
    body: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.size(width = 268.dp, height = 240.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = modifier.padding(16.dp)
                .fillMaxSize()
        ) {
            UserPreview(
                abbreviation = user.initials,
                title = user.fio,
                photoUri = user.photo,
                subtitle = user.summary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = body, style = MaterialTheme.typography.bodyMedium, maxLines = 4, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "читать далее...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
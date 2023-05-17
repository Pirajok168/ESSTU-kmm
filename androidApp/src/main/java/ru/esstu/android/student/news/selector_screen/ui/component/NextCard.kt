package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextCard(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.size(width = 268.dp, height = 240.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.ic_news_ornament_up), contentDescription = null)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Image(painter = painterResource(id = R.drawable.ic_news_ornament_down), contentDescription = null)
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Показать больше", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}


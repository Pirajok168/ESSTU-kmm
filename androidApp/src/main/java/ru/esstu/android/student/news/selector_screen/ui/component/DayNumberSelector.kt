package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayNumberSelector(modifier: Modifier = Modifier,weekNumber: Int, weekTitle: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(28.dp)
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weekNumber.toString(),
                color = MaterialTheme.colors.background,
                style = MaterialTheme.typography.h6.copy(fontSize = 16.sp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Text(text = weekTitle, style = MaterialTheme.typography.h6)

    }
}
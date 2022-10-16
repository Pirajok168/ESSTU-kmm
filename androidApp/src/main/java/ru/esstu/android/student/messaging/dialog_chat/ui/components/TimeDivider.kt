package ru.esstu.android.student.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz


private val formatterWithYear: DateFormat = DateFormat("dd MMMM yyyy")
private val formatter: DateFormat = DateFormat("dd MMMM")

@Composable
fun TimeDivider(date: DateTimeTz, isCurrentYear:Boolean) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.primary.copy(alpha = 0.1f))
                .padding(vertical = 4.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = if (isCurrentYear) formatter.format(date) else formatterWithYear.format(date),
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }
}
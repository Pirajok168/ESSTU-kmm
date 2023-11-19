package ru.esstu.android.shared.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageLoad

@Composable
fun AuthorChip(
    author: String,
    image: String? = null,
    date: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = author
        )

        date?.let {
            Text(
                text = date
            )
        }
    }
}
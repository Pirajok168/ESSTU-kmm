package ru.esstu.android.authorized.profile.ui.component

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.esstu.android.R

@Composable
fun MainInfoProfile(
    title: String,
    content: String,
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        tonalElevation = 32.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        onClick?.let {
            ListItem(
                headlineContent = {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.titleLarge,

                        )
                },
                overlineContent = {
                    Text(
                        text = title,
                    )
                },
                trailingContent = {
                    FilledTonalButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = null
                        )
                    }
                }
            )
        } ?:  ListItem(
            headlineContent = {
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleLarge,

                    )
            },
            overlineContent = {
                Text(
                    text = title,
                )
            }
        )

    }
}
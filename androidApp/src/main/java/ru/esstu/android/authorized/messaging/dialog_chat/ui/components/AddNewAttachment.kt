package ru.esstu.android.authorized.messaging.dialog_chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme

@Composable
fun AddNewAttachment(onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(60.dp),
            shape = CircleShape,
            tonalElevation = 16.dp,
            onClick = onClick
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

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.add_attachments),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Pppppqp() {
    CompPreviewTheme {
        Row {
            Box(modifier = Modifier.padding(vertical = 4.dp))
            {
                NewAttachment(title = "testfsdfsdfsdsdfsdfsdsdfsdf.txt")
            }
            Box(modifier = Modifier.padding(vertical = 4.dp))
            {
                AddNewAttachment()
            }

            Box(modifier = Modifier
                .height(107.dp)
                .widthIn(20.dp)
                .background(Color.Cyan))
        }
    }
}
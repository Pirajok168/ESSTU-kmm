package ru.android.esstu.feature.news.componentUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.android.esstu.core.theme.EsstuAppTheme
import ru.android.esstu.feature.news.NewsScreen
import ru.android.esstu.feature.news.R

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    label: String,
    underLabel: String,
    publicationDate: String,
    creator: String
) {

    Row(
        modifier = Modifier
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.essutm_logo_minimalism),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(4f)
                .padding(start = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = creator,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 10.sp
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                )

                Text(
                    text = publicationDate,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 10.sp
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 16.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = underLabel,
                maxLines = 3,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Box(modifier = Modifier, contentAlignment = Alignment.Center){
                IconButton(onClick = {  }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }
        }
    }
    

}


@Preview
@Composable
fun PreviewNewsCard() {
    EsstuAppTheme {
        NewsScreen()
    }
}
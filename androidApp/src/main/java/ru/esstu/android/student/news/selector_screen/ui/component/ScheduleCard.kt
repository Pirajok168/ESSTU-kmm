package ru.esstu.android.student.news.selector_screen.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.CompPreviewTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    modifier: Modifier = Modifier,
    previewText: String? = null,
    lessonName: String,
    lessonType: String,
    lessonTime: String,
    lessonNum: String,
    auditoriumNum: String,
    professorName: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 8.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val headerTextStyle = MaterialTheme.typography.bodyLarge

            Row {
                Image(
                    modifier = Modifier
                        .width(46.dp)
                        .padding(top = 8.dp),
                    painter = painterResource(id = R.drawable.ic_schedule_ornament),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(9.dp))
                Text(
                    text = lessonName,
                    style = headerTextStyle,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val body1Style = MaterialTheme.typography.bodyLarge
            val body2Style = MaterialTheme.typography.bodyMedium


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lessonTime, style = body2Style.copy(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {

                Text(text = professorName, style = body2Style.copy(fontSize = 16.sp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "|",
                    style = body1Style.copy(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ауд.",
                    style = body2Style.copy(fontSize = 16.sp)
                )
                Text(
                    text = auditoriumNum,
                    style = body2Style.copy(fontSize = 16.sp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))




            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = "$lessonNum-я пара",
                    style = body1Style.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "|",
                    style = body1Style.copy(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = lessonType,
                    style = body1Style.copy(fontSize = 16.sp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ppppp() {
    CompPreviewTheme {
        // ScheduleCard()
    }
}
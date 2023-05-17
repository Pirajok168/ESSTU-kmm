package ru.esstu.android.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.EsstuTheme

@Composable
fun LoginChooseScreen(
    onNavToAuth: () -> Unit = {},
    onNavToEntrant: () -> Unit = {},
) {
    Box(contentAlignment = Alignment.BottomEnd) {

        Image(
            modifier = Modifier
                .size(260.dp)
                .scale(1.2f),
            painter = painterResource(id = R.drawable.ic_auth_logo_borderless),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(Modifier.weight(1f)) {
                //Spacer(modifier = Modifier.height(98.dp))
                Text(
                    modifier = Modifier.paddingFromBaseline(128.dp),
                    text = "Добро пожаловать в личный кабинет ВСГУТУ",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(14.dp))
                //CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = "Выберите способ входа",
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
             //   }
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
                onClick = onNavToAuth) {
                Text(text = "Авторизация")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
                onClick = onNavToEntrant) {
                Text(text = "Я абитуриент")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}



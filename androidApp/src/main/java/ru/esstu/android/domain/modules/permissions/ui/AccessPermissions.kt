package ru.esstu.android.domain.modules.permissions.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import ru.esstu.android.R
import ru.esstu.android.domain.ui.theme.EsstuTheme
import ru.esstu.android.student.messaging.dialog_chat.viewmodel.DialogChatEvents

fun withPermissions(
    context: Context,
    vararg permissions: String,
    onRequest: () -> Unit,
    onGranted: () -> Unit
) {
    val hasPermissions = permissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }



    if (hasPermissions) onGranted() else onRequest()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AccessPermissions(
    onSkip: () -> Unit = {},
    onPermit: () -> Unit = {}
) {

    val context = LocalContext.current

    val permissionsLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { response ->

            val allPermissionsGranted =
                response.all { (_, isGranted) -> isGranted }

            if (allPermissionsGranted)
                onPermit()

        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(20.dp))
        Icon(
            painter = painterResource(id = R.drawable.baseline_notifications_24),
            contentDescription = "",
            tint = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier
                .size(50.dp)
                .padding(bottom = 10.dp)
        )

        Text(
            text = "Получайте уведомления!",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Будьте в курсе всех новостей вашего университета и моментально" +
                    " получайте уведомления о новых сообщениях",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onSkip,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondaryVariant)
                ) {
                    Text(text = "Пропустить")
                }
                
                Button(
                    onClick = {
                        withPermissions(
                            context = context.applicationContext,
                            permissions = arrayOf(
                                android.Manifest.permission.POST_NOTIFICATIONS,
                            ),
                            onGranted = onPermit,
                            onRequest = {
                                permissionsLauncher.launch(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS))
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(text = "Подтвердить")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAccessPermissions() {
    EsstuTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AccessPermissions()
        }else{
            Text("ыы лох")
        }
    }
}
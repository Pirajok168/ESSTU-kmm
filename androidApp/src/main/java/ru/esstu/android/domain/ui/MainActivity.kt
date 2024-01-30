package ru.esstu.android.domain.ui

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.esstu.ContextApplication
import ru.esstu.android.domain.navigation.SetupNavGraph
import ru.esstu.android.domain.ui.theme.AppEsstuTheme
import ru.esstu.android.notification.DefaultNotificationManager
import java.util.Locale
import androidx.compose.material3.AlertDialog
import ru.esstu.android.R


val Context.activity: AppCompatActivity
    get() = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.activity
        else -> error("No AppCompatActivity found in `$this`")
    }


@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_LOCALE = "SELECTED_LOCALE"
        const val SELECTED_LOCALE_TAG = "SELECTED_LOCALE_TAG"
    }

    //@Inject
   // lateinit var uiStateObserver: IUiObserver
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //observe activity lifecycle
        //lifecycle.addObserver(uiStateObserver)
        ContextApplication.init(applicationContext)
        //disables system insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppEsstuTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    SetupNavGraph(navController)
                }
                CheckNotificationsPermission(this)
            }
        }

    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun CheckNotificationsPermission(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            if (!areNotificationsEnabled) {
                ShowNotificationSettingsDialog()
            }
            return
        }

        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        if (notificationPermissionState.status is PermissionStatus.Denied) {
            LaunchedEffect(Unit) {
                launch {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ShowNotificationSettingsDialog() {
        var showGoToSettingsDialog by remember { mutableStateOf(true) }
        if (showGoToSettingsDialog) {
            AlertDialog(
                onDismissRequest = {
                    showGoToSettingsDialog = false
                },
                title = { Text(text = stringResource(R.string.notifications_disabled_title)) },
                text = { Text(text = stringResource(R.string.notifications_disabled_text)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data = Uri.fromParts("package", packageName, null)
                            startActivity(intent)
                            showGoToSettingsDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.go_to_settings))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showGoToSettingsDialog = false
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }


    override fun attachBaseContext(newBase: Context?) {

        val sharedPreferences = newBase?.getSharedPreferences(SELECTED_LOCALE, MODE_PRIVATE)
        val pine = sharedPreferences?.getString(SELECTED_LOCALE_TAG, "ru")

        super.attachBaseContext(MyContextWrapper.wrap(newBase,pine))
    }
}





class MyContextWrapper {
    companion object {
        @Suppress("deprecation")
        fun wrap(context: Context?, language: String?): Context? {
            var context = context
            val config: Configuration = context?.resources?.configuration ?: return context
            if (language != "") {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale)
                } else {
                    setSystemLocaleLegacy(config, locale)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context = context.createConfigurationContext(config)
            } else {
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
            return context
        }


        @Suppress("deprecation")
        private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale?) {
            config.setLocale(locale)
        }
    }

}


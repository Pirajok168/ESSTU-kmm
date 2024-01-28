package ru.esstu.android.domain.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.esstu.ContextApplication
import ru.esstu.android.domain.navigation.SetupNavGraph
import ru.esstu.android.domain.ui.theme.AppEsstuTheme
import java.util.Locale

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
            }
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


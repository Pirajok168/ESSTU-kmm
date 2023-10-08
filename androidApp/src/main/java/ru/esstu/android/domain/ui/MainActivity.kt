package ru.esstu.android.domain.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

import androidx.core.view.WindowCompat

import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import dagger.hilt.android.AndroidEntryPoint

import ru.esstu.android.domain.navigation.SetupNavGraph
import ru.esstu.android.domain.ui.theme.EsstuTheme



@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {

    //@Inject
   // lateinit var uiStateObserver: IUiObserver
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //observe activity lifecycle
        //lifecycle.addObserver(uiStateObserver)

        //disables system insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            EsstuTheme {

                val context = LocalContext.current

                val navController = rememberNavController()

                //observe current destination
                //SetupUiObserver(navController = navController, listener = uiStateObserver)
                Surface(
                    color = MaterialTheme.colors.background,
                ) {

                    SetupNavGraph(navController)
                }


            }
        }
    }
}



package ru.esstu.android.domain.ui

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

import ru.esstu.android.domain.ui.theme.AppEsstuTheme
import dagger.hilt.android.AndroidEntryPoint

import ru.esstu.android.domain.navigation.SetupNavGraph


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
}



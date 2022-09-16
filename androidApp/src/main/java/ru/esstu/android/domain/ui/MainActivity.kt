package ru.esstu.android.domain.ui

import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.core.view.WindowCompat

import androidx.navigation.compose.rememberNavController

import ru.esstu.android.domain.navigation.SetupNavGraph
import ru.esstu.android.domain.ui.theme.EsstuTheme



class MainActivity : AppCompatActivity() {

    //@Inject
   // lateinit var uiStateObserver: IUiObserver
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //observe activity lifecycle
        //lifecycle.addObserver(uiStateObserver)

        //disables system insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            EsstuTheme {


                val navController = rememberNavController()

                //observe current destination
                //SetupUiObserver(navController = navController, listener = uiStateObserver)
                Surface() {
                    SetupNavGraph(navController)
                }


            }
        }
    }
}



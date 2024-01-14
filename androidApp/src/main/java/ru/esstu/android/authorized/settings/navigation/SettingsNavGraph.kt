package ru.esstu.android.authorized.settings.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.settings.ui.LocaleSettingsScreen
import ru.esstu.android.authorized.settings.ui.SettingsScreen

class SettingsNavGraph {
}

fun NavGraphBuilder.settingsNavGraph(
    parentNavController: NavHostController,
    navController: NavHostController,
    padding: PaddingValues
) {

    navigation(
        route = SettingsScreens.Root.passRoute(),
        startDestination = SettingsScreens.Main.startDest()
    ) {
        composable(
            route = SettingsScreens.Main.passRoute(),
        ) {
            SettingsScreen(padding, toLocaleApp = {navController.navigate(SettingsScreens.LocaleApp.navigate())})
        }

        composable(
            route = SettingsScreens.LocaleApp.passRoute(),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = {
                        it / 2
                    }
                )
            },
            exitTransition = {
                slideOutHorizontally {
                    it / 2
                }
            }
        ) {
            LocaleSettingsScreen(padding, onBackPressed = {navController.popBackStack()})
        }
    }
}
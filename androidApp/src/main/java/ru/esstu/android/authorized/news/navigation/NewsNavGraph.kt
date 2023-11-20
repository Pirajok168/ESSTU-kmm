package ru.esstu.android.authorized.news.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.news.ui.DetailNewsScreen
import ru.esstu.android.authorized.news.ui.MainScreen


fun NavGraphBuilder.newsNavGraph(
    padding: PaddingValues,
    parentNavController: NavHostController,
    navController: NavHostController
) {
    navigation(
        route = NewsScreens.Root.passRoute(),
        startDestination = NewsScreens.SelectorScreen.startDest()
    ) {
        composable(
            route = NewsScreens.SelectorScreen.passRoute(),

        ) {
            MainScreen(
                parentPadding = padding,
                onDetailNews = {
                    navController.navigate(NewsScreens.DetailScreen.navigate())
                }
            )
        }

        composable(
            route = NewsScreens.DetailScreen.passRoute(),
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
            val parent = remember(it) {
                navController.getBackStackEntry(NewsScreens.SelectorScreen.popTo())
            }

            DetailNewsScreen(
                parentPadding = padding,
                onBackPressed = { navController.popBackStack() },
                selectorViewModel = viewModel(parent)
            )
        }

        /*
        composable(route = NewsScreens.NewsScreen.passRoute()) {

            val parent = remember {
                navController.getBackStackEntry(NewsScreens.SelectorScreen.popTo())
            }

            NewsScreen(
                parentPadding = padding,
                onBackPressed = { navController.popBackStack() },
                onNavToImageScreen = { selected, uris ->
                    parentNavController.navigate(ImageScreen.navigate(images = uris, startImage = selected))
                },
                hiltViewModel(parent)
            )
        }



        composable(route = NewsScreens.EventsScreen.passRoute()) {
            val parent = remember {
                navController.getBackStackEntry(NewsScreens.SelectorScreen.popTo())
            }

            EventsScreen(
                parentPadding = padding,
                onBackPressed = { navController.popBackStack() },
                onNavToImageScreen = { selected, uris ->
                    parentNavController.navigate(ImageScreen.navigate(images = uris, startImage = selected))
                },
                hiltViewModel(parent)
            )
        }*/
    }
}
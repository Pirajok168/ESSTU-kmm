package ru.esstu.android.student.news.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.domain.modules.image_viewer.navigation.ImageScreen
import ru.esstu.android.student.news.announcement_screen.ui.AnnouncementScreen
import ru.esstu.android.student.news.selector_screen.ui.DetailNewsScreen
import ru.esstu.android.student.news.selector_screen.ui.NewsSelectorScreen


fun NavGraphBuilder.newsNavGraph(
    padding: PaddingValues,
    parentNavController: NavHostController,
    navController: NavHostController
) {
    navigation(
        route = NewsScreens.Root.passRoute(),
        startDestination = NewsScreens.SelectorScreen.startDest()
    ) {
        composable(route = NewsScreens.SelectorScreen.passRoute()) {
            NewsSelectorScreen(
                parentPadding = padding,
                onNavToAnnouncements = {
                    navController.navigate(NewsScreens.AnnouncementsScreen.navigate())
                },
                onNavToEvents = {
                    navController.navigate(NewsScreens.EventsScreen.navigate())
                },
                onNavToNews = {
                    navController.navigate(NewsScreens.NewsScreen.navigate())
                },
                onNavToDetailScreen = {
                    navController.navigate(NewsScreens.DetailScreen.navigate())
                },
                onNavToScheduleScreen = {
                    /*navController.navigate(ScheduleScreen.passRoute()) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }*/
                }
            )
        }

        composable(route = NewsScreens.DetailScreen.passRoute()) {
            val parent = remember {
                navController.getBackStackEntry(NewsScreens.SelectorScreen.popTo())
            }

            DetailNewsScreen(
                parentPadding = padding,
                onBackPressed = { navController.popBackStack() },
                onNavToImageScreen = { selected, uris ->
                    parentNavController.navigate(ImageScreen.navigate(images = uris, startImage = selected))
                },
                selectorViewModel = viewModel(parent)
            )
        }

        composable(route = NewsScreens.AnnouncementsScreen.passRoute()) {
            val parent = remember {
                navController.getBackStackEntry(NewsScreens.SelectorScreen.popTo())
            }

            AnnouncementScreen(
                parentPadding = padding,
                onBackPressed = { navController.popBackStack() },
                onNavToImageScreen = { selected, uris ->
                    parentNavController.navigate(ImageScreen.navigate(images = uris, startImage = selected))
                },
                viewModel(parent)
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
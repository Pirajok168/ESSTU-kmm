package ru.esstu.android.authorized.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.navigation.bottom_navigation.BottomAuthorizedNavigation
import ru.esstu.android.student.navigation.StudentRoutes

fun NavGraphBuilder.authorizedNavigationGraph(
    navController: NavHostController
) {
    navigation(
        route = StudentRoutes.Root.passRoute(),
        startDestination = StudentRoutes.BottomNavScreen.startDest(),
    ) {
        composable(
            route = StudentRoutes.BottomNavScreen.passRoute(),
            deepLinks = StudentRoutes.BottomNavScreen.passDeepLinkRoute()
        ) {
            BottomAuthorizedNavigation(parentNavController = navController)
        }
    }
}
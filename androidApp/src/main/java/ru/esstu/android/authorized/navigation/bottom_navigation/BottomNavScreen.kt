package ru.esstu.android.authorized.navigation.bottom_navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.esstu.android.domain.navigation.bottom_navigation.util.NavItem
import ru.esstu.android.domain.navigation.Route

@Composable
fun BottomNavScreen(
    navItems: List<NavItem>,
    startDestination: Route<*>,
    contentNavGraph: NavGraphBuilder.(padding: PaddingValues, inlineNavController: NavHostController) -> Unit
) {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach {navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route.startDest() } == true,
                        onClick = {
                            navController.navigate(navItem.route.passRoute()) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = { Icon( painter = painterResource(id = navItem.icon), contentDescription = null) }
                    )
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination.startDest()) {
            contentNavGraph(it, navController)
        }
    }
}
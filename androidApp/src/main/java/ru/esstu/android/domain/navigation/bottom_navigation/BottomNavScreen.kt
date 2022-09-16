package ru.esstu.android.domain.navigation.bottom_navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.esstu.android.domain.navigation.bottom_navigation.util.NavItem
import ru.esstu.android.domain.navigation.Route

@Composable
fun BottomNavScreen(
    modifier: Modifier = Modifier,
    navItems: Array<NavItem>,
    appBarContent: @Composable (selectedDestination: Route<*>) -> Unit = { },
    startDestination: Route<*>,
    contentNavGraph: NavGraphBuilder.(padding: PaddingValues, inlineNavController: NavHostController) -> Unit
) {
    val navController: NavHostController = rememberNavController()
    var selectedTab by remember { mutableStateOf(startDestination) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { appBarContent(selectedTab) },
        bottomBar = {
            BottomBar(
                modifier = modifier,
                navController = navController,
                tab = navItems,
            ) { selectedTab = it }
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination.startDest()) {
            contentNavGraph(it, navController)
        }
    }
}
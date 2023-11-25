package ru.esstu.android.authorized.messaging.new_message.new_support.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.authorized.messaging.new_message.new_support.ui.NewSupportScreen
import ru.esstu.android.authorized.messaging.new_message.new_support.ui.NewSupportThemeSelectorScreen
import ru.esstu.android.authorized.messaging.new_message.selector.navigation.SelectorScreens


fun NavGraphBuilder.newSupportNavGraph(
    navController: NavHostController
) {
    navigation(
        route = NewSupportScreens.Root.passRoute(),
        startDestination = NewSupportScreens.NewSupportScreen.startDest()
    ) {
        composable(route = NewSupportScreens.NewSupportScreen.passRoute()) {
            NewSupportScreen(
                onBackPress = { navController.popBackStack() },
                onNavToSupportChat = {
                    navController.navigate(GroupChatScreen.navigate(it)) {
                        popUpTo(SelectorScreens.Selector.popTo()) {
                            inclusive = true
                        }
                    }
                },
                onNavToThemeSelector = {
                    navController.navigate(NewSupportScreens.NewSupportThemeSelectorScreen.navigate())
                }
            )
        }

        composable(route = NewSupportScreens.NewSupportThemeSelectorScreen.passRoute()) {
            val parent = remember(it) {
                navController.getBackStackEntry(NewSupportScreens.NewSupportScreen.popTo())
            }

            NewSupportThemeSelectorScreen(
                onBackPressed = { navController.popBackStack() },
                viewModel = hiltViewModel(parent)
            )
        }
    }
}
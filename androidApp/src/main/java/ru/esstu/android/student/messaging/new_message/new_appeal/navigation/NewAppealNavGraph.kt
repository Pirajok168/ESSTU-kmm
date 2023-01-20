package ru.esstu.android.student.messaging.new_message.new_appeal.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.student.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.student.messaging.new_message.new_appeal.ui.NewAppealDepartmentSelectorScreen
import ru.esstu.android.student.messaging.new_message.new_appeal.ui.NewAppealScreen
import ru.esstu.android.student.messaging.new_message.new_appeal.ui.NewAppealThemeSelectorScreen
import ru.esstu.android.student.messaging.new_message.selector.navigation.SelectorScreens

fun NavGraphBuilder.newAppealNavGraph(navController: NavHostController) {
    navigation(
        route = NewAppealsScreens.Root.passRoute(),
        startDestination = NewAppealsScreens.NewAppealsScreen.startDest(),
    ) {
        composable(route = NewAppealsScreens.NewAppealsScreen.passRoute()) {
            NewAppealScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToDepartmentSelector = { navController.navigate(NewAppealsScreens.NewAppealsDepartmentSelectionScreen.navigate()) },
                onNavToThemeSelector = { navController.navigate(NewAppealsScreens.NewAppealsThemeSelectionScreen.navigate()) },
                onNavToAppealChat = {
                    navController.navigate(GroupChatScreen.navigate(it)) {
                        popUpTo(SelectorScreens.Selector.popTo()) {
                            inclusive = true
                        }
                    }
                }

            )
        }

        composable(route = NewAppealsScreens.NewAppealsDepartmentSelectionScreen.passRoute()) {
            val parent = remember(it) {
                navController.getBackStackEntry(NewAppealsScreens.NewAppealsScreen.popTo())
            }

            NewAppealDepartmentSelectorScreen(
                onBackPressed = { navController.popBackStack() },
                viewModel = hiltViewModel(parent)
            )
        }

        composable(route = NewAppealsScreens.NewAppealsThemeSelectionScreen.passRoute()) {
            val parent = remember(it) {
                navController.getBackStackEntry(NewAppealsScreens.NewAppealsScreen.popTo())
            }

            NewAppealThemeSelectorScreen(
                onBackPressed = { navController.popBackStack() },
                viewModel = hiltViewModel(parent)
            )
        }
    }
}
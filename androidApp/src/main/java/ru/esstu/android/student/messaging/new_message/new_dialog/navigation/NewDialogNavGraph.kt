package ru.esstu.android.student.messaging.new_message.new_dialog.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.student.messaging.new_message.new_dialog.ui.NewDialogScreen
import ru.esstu.android.student.messaging.new_message.new_dialog.ui.NewDialogSearchScreen
import ru.esstu.android.student.messaging.new_message.selector.navigation.SelectorScreens

fun NavGraphBuilder.newDialogNavGraph(
    navController: NavHostController
) {
    navigation(
        route = NewDialogScreens.Root.passRoute(),
        startDestination = NewDialogScreens.NewDialogScreen.startDest()
    ) {
        composable(route = NewDialogScreens.NewDialogScreen.passRoute()) {
            NewDialogScreen(
                onBackPressed = { navController.popBackStack() },
                onNavToSearchScreen = {
                    navController.navigate(NewDialogScreens.SearchScreen.navigate())
                },
                onNavToDialogChat = {
                    navController.navigate(DialogChatScreen.navigate(it)){
                        popUpTo(SelectorScreens.Selector.popTo()) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = NewDialogScreens.SearchScreen.passRoute()) {

            val parent = remember(it) {
                navController.getBackStackEntry(NewDialogScreens.NewDialogScreen.popTo())
            }

            NewDialogSearchScreen(
                onBackPress = { navController.popBackStack() },
                viewModel = viewModel(parent)
            )
        }
    }
}
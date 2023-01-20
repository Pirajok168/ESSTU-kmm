package ru.esstu.android.student.messaging.new_message.selector.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.student.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.student.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.student.messaging.new_message.new_appeal.navigation.NewAppealsScreens
import ru.esstu.android.student.messaging.new_message.new_appeal.navigation.newAppealNavGraph
import ru.esstu.android.student.messaging.new_message.new_dialog.navigation.NewDialogScreens
import ru.esstu.android.student.messaging.new_message.new_dialog.navigation.newDialogNavGraph
import ru.esstu.android.student.messaging.new_message.new_support.navigation.NewSupportScreens
import ru.esstu.android.student.messaging.new_message.new_support.navigation.newSupportNavGraph
import ru.esstu.android.student.messaging.new_message.selector.ui.NewMessageSelectorScreen

fun NavGraphBuilder.newMsgSelectorNavGraph(
    navController: NavHostController
) {
    navigation(
        route = SelectorScreens.Root.passRoute(),
        startDestination = SelectorScreens.Selector.passRoute()
    ) {
        composable(route = SelectorScreens.Selector.passRoute()) {
            NewMessageSelectorScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onNavToDialogChat = {
                    navController.navigate(DialogChatScreen.navigate(it)) {
                        popUpTo(SelectorScreens.Selector.popTo()) {
                            inclusive = true
                        }
                    }
                },
                onNavToConversationChat = {
                    navController.navigate(GroupChatScreen.navigate(it.toInt())) {
                        popUpTo(SelectorScreens.Selector.popTo()) {
                            inclusive = true
                        }
                    }
                },
                onNavToNewDialog = {
                    navController.navigate(NewDialogScreens.NewDialogScreen.navigate())
                },
                onNavToNewConversation = {

                },
                onNavToNewTechSupport = {
                    navController.navigate(NewSupportScreens.NewSupportScreen.navigate())
                },
                onNavToNewAllies = {
                   navController.navigate(NewAppealsScreens.NewAppealsScreen.navigate())
                }
            )
        }

        newDialogNavGraph(navController = navController)



        newSupportNavGraph(navController = navController)

        newAppealNavGraph(navController = navController)
    }
}
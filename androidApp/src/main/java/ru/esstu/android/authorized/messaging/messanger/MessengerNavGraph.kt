package ru.esstu.android.authorized.messaging.messanger

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.student.messaging.new_message.selector.navigation.SelectorScreens

fun NavGraphBuilder.messengerNavGraph(
    parentNavController: NavHostController,
    NavController: NavHostController,
    padding: PaddingValues
) {
    navigation(
        route = MessengerScreens.Root.passRoute(),
        startDestination = MessengerScreens.Messenger.startDest()
    ) {
        composable(route = MessengerScreens.Messenger.passRoute()) {
            MessengerScreen(
                parentPadding = padding,
                onNavToNewMessage = {
                    parentNavController.navigate(SelectorScreens.Selector.passRoute())
                },
                onNavToDialogChat = {
                    parentNavController.navigate(DialogChatScreen.navigate(it))
                },
                onNavToConversationChat = {
                    parentNavController.navigate(GroupChatScreen.navigate(it))
                },
                onNavToAppealChat = {
                    parentNavController.navigate(GroupChatScreen.navigate(it))
                },
                onNavToSupportChat = {
                    parentNavController.navigate(GroupChatScreen.navigate(it))
                }
            )
        }

        composable(route = MessengerScreens.SearchScreen.passRoute()) {
            /* TODO */
        }
    }
}
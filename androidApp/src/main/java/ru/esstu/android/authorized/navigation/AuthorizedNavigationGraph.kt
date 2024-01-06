package ru.esstu.android.authorized.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatArguments
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.dialog_chat.ui.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatArguments
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.authorized.messaging.group_chat.ui.GroupChatScreen
import ru.esstu.android.authorized.messaging.new_message.selector.navigation.newMsgSelectorNavGraph
import ru.esstu.android.authorized.navigation.bottom_navigation.BottomAuthorizedNavigation
import ru.esstu.android.domain.modules.image_viewer.navigation.ImageScreen
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

        composable(
            route = DialogChatScreen.passRoute(),
            deepLinks = DialogChatScreen.passDeepLinkRoute(),
            arguments = DialogChatScreen.passArguments(),
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
            val opponentId = it.arguments?.getString(DialogChatArguments.OPPONENT_ID.name)
            if (opponentId != null)
                DialogChatScreen(
                    opponentId = opponentId,
                    onBackPressed = { navController.popBackStack() },
                    onNavToImage = { uri, uris ->
                        navController.navigate(ImageScreen.navigate(uri, uris))
                    }
                )
        }

        composable(
            route = GroupChatScreen.passRoute(),
            arguments = GroupChatScreen.passArguments(),
            deepLinks = GroupChatScreen.passDeepLinkRoute(),
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
            val convIdStr = it.arguments?.getString(GroupChatArguments.GROUP_ID.name)
            val convId = convIdStr?.toInt()

            if (convId != null)
                GroupChatScreen(
                    convId = convId,
                    onBackPressed = { navController.popBackStack() },
                    onNavToImage = { uri, uris ->
                        navController.navigate(ImageScreen.navigate(uri, uris))
                    }
                )
        }
        newMsgSelectorNavGraph(navController)

    }
}
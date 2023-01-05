package ru.esstu.android.student.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.domain.modules.image_viewer.navigation.ImageScreen
import ru.esstu.android.student.messaging.dialog_chat.navigation.DialogChatArguments
import ru.esstu.android.student.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.student.messaging.dialog_chat.ui.DialogChatScreen



@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi

@ExperimentalMaterialApi
fun NavGraphBuilder.studentNavGraph(
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
            StudentBottomNavScreen(parentNavController = navController)
        }
        composable(
            route = DialogChatScreen.passRoute(),
            arguments = DialogChatScreen.passArguments(),
            deepLinks = DialogChatScreen.passDeepLinkRoute()
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

        /*composable(
            route = GroupChatScreen.passRoute(),
            arguments = GroupChatScreen.passArguments(),
            deepLinks = GroupChatScreen.passDeepLinkRoute()
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
        }*/
    /*



        newMsgSelectorNavGraph(navController = navController)

        newPortfolioSelectorNavGraph(navController = navController)

        composable(
            route = JournalScreen.passRoute(),
            arguments = JournalScreen.passArguments()
        ) {
            val journalId = it.arguments?.getString(JournalArgs.JournalId.name)?.toIntOrNull()

            if (journalId != null) {
                JournalScreen(journalId = journalId, onBackPress = { navController.popBackStack() })
            }
        }*/
    }
}


package ru.esstu.android.authorized.student.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.authorized.messaging.new_message.selector.navigation.SelectorScreens
import ru.esstu.android.authorized.student.profile.ui.StudentProfileScreen

fun NavGraphBuilder.profileNavGraph(
    parentNavController: NavHostController,
    NavController: NavHostController,
    padding: PaddingValues
) {
    navigation(
        route = ProfileScreens.Root.passRoute(),
        startDestination = ProfileScreens.Profile.startDest()
    ) {
        composable(route = ProfileScreens.Profile.passRoute()) {
            StudentProfileScreen()
        }

    }
}
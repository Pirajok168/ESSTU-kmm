package ru.esstu.android.authorized.student.profile.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import ru.esstu.ESSTUSdk
import ru.esstu.android.authorized.messaging.dialog_chat.navigation.DialogChatScreen
import ru.esstu.android.authorized.messaging.group_chat.navigation.GroupChatScreen
import ru.esstu.android.authorized.messaging.new_message.selector.navigation.SelectorScreens
import ru.esstu.android.authorized.student.profile.ui.StudentProfileScreen
import ru.esstu.student.profile.student.porfolio.domain.di.portfolioModule
import ru.esstu.student.profile.student.porfolio.domain.model.PortfolioType
import ru.esstu.student.profile.student.porfolio.domain.repository.IPortfolioRepository

fun NavGraphBuilder.profileNavGraph(
    parentNavController: NavHostController,
    navController: NavHostController,
    padding: PaddingValues
) {
    navigation(
        route = ProfileScreens.Root.passRoute(),
        startDestination = ProfileScreens.Profile.startDest()
    ) {
        composable(route = ProfileScreens.Profile.passRoute()) {
            StudentProfileScreen(padding){
                navController.navigate(ProfileScreens.Awards.navigate())
            }
        }

        composable(route = ProfileScreens.Awards.passRoute()) {
            val repo: IPortfolioRepository = ESSTUSdk.portfolioModule.repo
            val scope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit, block = {
                scope.launch {
                    repo.getFilesPortfolioByType(PortfolioType.AWARD).data?.let {
                        Log.e("drgdrgrd", "$it")
                    }
                }
            })

        }
    }
}